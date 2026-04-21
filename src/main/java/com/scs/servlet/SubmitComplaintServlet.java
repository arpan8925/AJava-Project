package com.scs.servlet;

import com.scs.dao.AnalyticsLogDAO;
import com.scs.dao.ComplaintDAO;
import com.scs.dao.ComplaintReplyDAO;
import com.scs.dao.DepartmentDAO;
import com.scs.dao.NotificationDAO;
import com.scs.dao.UserDAO;
import com.scs.model.AnalyticsLog;
import com.scs.model.Complaint;
import com.scs.model.Complaint.Priority;
import com.scs.model.ComplaintReply;
import com.scs.model.Department;
import com.scs.model.Notification;
import com.scs.model.User;
import com.scs.util.AutoReplyGenerator;
import com.scs.util.EmergencyDetector;
import com.scs.util.ETAPredictor;
import com.scs.util.FileUploadUtil;
import com.scs.util.PriorityBooster;
import com.scs.util.SentimentAnalyzer;
import com.scs.util.SmartRecommender;
import com.scs.util.SmartSummarizer;
import com.scs.util.TagGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet("/user/submit-complaint")
@MultipartConfig(
        fileSizeThreshold = 512 * 1024,
        maxFileSize       = 5 * 1024 * 1024,
        maxRequestSize    = 10 * 1024 * 1024
)
public class SubmitComplaintServlet extends HttpServlet {

    private final ComplaintDAO       complaintDAO     = new ComplaintDAO();
    private final ComplaintReplyDAO  replyDAO         = new ComplaintReplyDAO();
    private final NotificationDAO    notificationDAO  = new NotificationDAO();
    private final AnalyticsLogDAO    analyticsDAO     = new AnalyticsLogDAO();
    private final DepartmentDAO      departmentDAO    = new DepartmentDAO();
    private final UserDAO            userDAO          = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("departments", departmentDAO.findAll());
        req.setAttribute("categories",  SmartRecommender.supportedCategories());
        req.getRequestDispatcher("/jsp/user/submit-complaint.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User author = (User) session.getAttribute("loggedInUser");

        String title       = trim(req.getParameter("title"));
        String description = trim(req.getParameter("description"));
        String category    = trim(req.getParameter("category"));
        String location    = trim(req.getParameter("location"));
        String deptParam   = trim(req.getParameter("departmentId"));

        if (title == null || title.isEmpty() || description == null || description.isEmpty()) {
            req.setAttribute("error", "Title and description are required.");
            doGet(req, resp);
            return;
        }

        // Step 1: save uploaded image (if any)
        String imagePath = null;
        try {
            Part image = req.getPart("image");
            if (image != null && image.getSize() > 0) {
                String uploadDir = resolveUploadDir(req);
                imagePath = FileUploadUtil.save(image, uploadDir);
            }
        } catch (IOException | IllegalStateException e) {
            req.setAttribute("error", "Image upload failed: " + e.getMessage());
            doGet(req, resp);
            return;
        }

        String combined = title + " " + description;

        // Step 2: emergency detection
        EmergencyDetector.Result emergency = EmergencyDetector.detect(title, description);

        // Step 3: sentiment analysis → initial priority
        SentimentAnalyzer.Result sentiment = SentimentAnalyzer.analyze(combined);
        Priority priority = sentiment.priority;

        // Step 4: priority boost based on user history
        Date thirtyDaysAgo = daysAgo(30);
        Date sevenDaysAgo  = daysAgo(7);
        long recentCount      = complaintDAO.countByUserSince(author.getId(), thirtyDaysAgo);
        long staleUnresolved  = complaintDAO.countPendingByUserOlderThan(author.getId(), sevenDaysAgo);
        priority = PriorityBooster.boost(priority, (int) recentCount, (int) staleUnresolved);

        // Step 5: emergency override
        if (emergency.isEmergency) priority = Priority.CRITICAL;

        // Step 6: summary
        String summary = SmartSummarizer.summarize(description);

        // Step 7: tags
        String tags = TagGenerator.generateTagsString(title, description);

        // Step 8: ETA (dept load = 0 since dept not yet assigned)
        boolean repeatUser = recentCount > 3;
        int eta = ETAPredictor.predictETA(priority, 0, repeatUser);

        // category auto-detection if user didn't pick one
        if (category == null || category.isEmpty()) {
            category = detectCategory(title, description);
        }

        // optional dept auto-pick by category
        Department dept = null;
        if (deptParam != null && !deptParam.isEmpty()) {
            try {
                dept = departmentDAO.findById(Long.parseLong(deptParam));
            } catch (NumberFormatException ignored) { /* fall through */ }
        }

        // Step 9: save complaint
        Complaint c = new Complaint();
        User authorRef = userDAO.findById(author.getId());
        c.setUser(authorRef);
        c.setDepartment(dept);
        c.setTitle(title);
        c.setDescription(description);
        c.setSummary(summary);
        c.setTags(tags);
        c.setCategory(category);
        c.setSentiment(sentiment.sentiment);
        c.setPriority(priority);
        c.setStatus(Complaint.Status.PENDING);
        c.setImagePath(imagePath);
        c.setLocation(location != null && !location.isEmpty() ? location : author.getArea());
        c.setEtaHours(eta);
        c.setIsEmergency(emergency.isEmergency);
        Long id = complaintDAO.save(c);

        // Step 10: auto-reply + citizen notification
        Complaint saved = complaintDAO.findById(id);
        String replyText = AutoReplyGenerator.generateReply(saved);
        User systemAuthor = userDAO.findById(1L);
        replyDAO.save(new ComplaintReply(saved, systemAuthor != null ? systemAuthor : authorRef, replyText));
        notificationDAO.save(new Notification(authorRef,
                "Your complaint #" + id + " has been received. Priority: " + priority + ", ETA: " + eta + " hours."));

        // Step 10b: admin notification for emergencies
        if (emergency.isEmergency) {
            for (User admin : userDAO.findByRole(User.Role.ADMIN)) {
                notificationDAO.save(new Notification(admin,
                        "EMERGENCY complaint #" + id + " reported at "
                                + (saved.getLocation() == null ? "unknown location" : saved.getLocation()) + "."));
            }
        }

        // Step 12: analytics log
        analyticsDAO.save(new AnalyticsLog("COMPLAINT_CREATED", id,
                dept != null ? dept.getId() : null, saved.getLocation()));
        if (emergency.isEmergency) {
            analyticsDAO.save(new AnalyticsLog("EMERGENCY_FLAGGED", id,
                    dept != null ? dept.getId() : null, saved.getLocation()));
        }

        // Step 11: redirect to track page; similar complaints shown there via AJAX
        resp.sendRedirect(req.getContextPath() + "/user/track-complaint?id=" + id);
    }

    private String resolveUploadDir(HttpServletRequest req) {
        String fromCtx = req.getServletContext().getInitParameter("upload.dir");
        String base = fromCtx == null || fromCtx.isEmpty() ? "uploads" : fromCtx;
        File absolute = new File(base);
        if (!absolute.isAbsolute()) {
            absolute = new File(System.getProperty("user.home"), "scs-uploads");
        }
        return absolute.getAbsolutePath();
    }

    private String detectCategory(String title, String description) {
        return SmartRecommender.detectCategory(title, description);
    }

    private static Date daysAgo(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        return cal.getTime();
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
}
