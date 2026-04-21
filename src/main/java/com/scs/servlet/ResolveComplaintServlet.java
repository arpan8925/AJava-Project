package com.scs.servlet;

import com.scs.dao.AnalyticsLogDAO;
import com.scs.dao.ComplaintDAO;
import com.scs.dao.ComplaintReplyDAO;
import com.scs.dao.NotificationDAO;
import com.scs.dao.UserDAO;
import com.scs.model.AnalyticsLog;
import com.scs.model.Complaint;
import com.scs.model.Complaint.Status;
import com.scs.model.ComplaintReply;
import com.scs.model.Notification;
import com.scs.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/officer/resolve")
public class ResolveComplaintServlet extends HttpServlet {

    private final ComplaintDAO       complaintDAO    = new ComplaintDAO();
    private final ComplaintReplyDAO  replyDAO        = new ComplaintReplyDAO();
    private final NotificationDAO    notificationDAO = new NotificationDAO();
    private final UserDAO            userDAO         = new UserDAO();
    private final AnalyticsLogDAO    analyticsDAO    = new AnalyticsLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = parseLong(req.getParameter("id"));
        if (id == null) {
            resp.sendRedirect(req.getContextPath() + "/officer/complaints");
            return;
        }
        Complaint c = complaintDAO.findById(id);
        if (c == null) {
            resp.sendRedirect(req.getContextPath() + "/officer/complaints");
            return;
        }
        List<ComplaintReply> replies = replyDAO.findByComplaintId(id);

        req.setAttribute("complaint", c);
        req.setAttribute("replies", replies);
        req.getRequestDispatcher("/jsp/officer/resolve.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User officer = (User) session.getAttribute("loggedInUser");
        Long id = parseLong(req.getParameter("complaintId"));
        String action = req.getParameter("action");
        String message = req.getParameter("message");

        if (id == null || action == null) {
            resp.sendRedirect(req.getContextPath() + "/officer/complaints");
            return;
        }

        Complaint c = complaintDAO.findById(id);
        if (c == null) {
            resp.sendRedirect(req.getContextPath() + "/officer/complaints");
            return;
        }

        User officerRef = userDAO.findById(officer.getId());
        String eventType = null;
        String notifyMessage = null;

        switch (action) {
            case "start":
                if (c.getStatus() == Status.ASSIGNED || c.getStatus() == Status.PENDING) {
                    c.setStatus(Status.IN_PROGRESS);
                    complaintDAO.update(c);
                    eventType = "COMPLAINT_IN_PROGRESS";
                    notifyMessage = "Your complaint #" + id + " is now IN PROGRESS.";
                }
                break;
            case "resolve":
                c.setStatus(Status.RESOLVED);
                c.setResolvedAt(new Date());
                complaintDAO.update(c);
                eventType = "COMPLAINT_RESOLVED";
                notifyMessage = "Your complaint #" + id + " has been marked RESOLVED.";
                break;
            case "reopen":
                c.setStatus(Status.IN_PROGRESS);
                c.setResolvedAt(null);
                complaintDAO.update(c);
                eventType = "COMPLAINT_REOPENED";
                notifyMessage = "Your complaint #" + id + " was reopened.";
                break;
            case "reply":
                // handled below
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/officer/resolve?id=" + id);
                return;
        }

        if (message != null && !message.trim().isEmpty()) {
            replyDAO.save(new ComplaintReply(c, officerRef, message.trim()));
        }

        if (c.getUser() != null && notifyMessage != null) {
            notificationDAO.save(new Notification(c.getUser(), notifyMessage));
        }
        if (eventType != null) {
            Long deptId = c.getDepartment() == null ? null : c.getDepartment().getId();
            analyticsDAO.save(new AnalyticsLog(eventType, id, deptId, c.getLocation()));
        }

        resp.sendRedirect(req.getContextPath() + "/officer/resolve?id=" + id);
    }

    private static Long parseLong(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
    }
}
