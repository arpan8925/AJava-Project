package com.scs.servlet;

import com.scs.dao.ComplaintDAO;
import com.scs.dao.ComplaintReplyDAO;
import com.scs.model.Complaint;
import com.scs.model.ComplaintReply;
import com.scs.model.User;
import com.scs.util.SmartRecommender;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/track-complaint")
public class TrackComplaintServlet extends HttpServlet {

    private final ComplaintDAO       complaintDAO = new ComplaintDAO();
    private final ComplaintReplyDAO  replyDAO     = new ComplaintReplyDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/user/my-complaints");
            return;
        }

        long id;
        try { id = Long.parseLong(idParam); }
        catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/user/my-complaints");
            return;
        }

        Complaint complaint = complaintDAO.findById(id);
        User user = (User) req.getSession().getAttribute("loggedInUser");

        if (complaint == null
                || complaint.getUser() == null
                || !complaint.getUser().getId().equals(user.getId())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.getRequestDispatcher("/jsp/error/access-denied.jsp").forward(req, resp);
            return;
        }

        List<ComplaintReply> replies = replyDAO.findByComplaintId(id);
        List<String> recommendations = SmartRecommender.recommend(
                complaint.getTitle(), complaint.getDescription(), complaint.getCategory());

        req.setAttribute("complaint", complaint);
        req.setAttribute("replies", replies);
        req.setAttribute("recommendations", recommendations);
        req.getRequestDispatcher("/jsp/user/track-complaint.jsp").forward(req, resp);
    }
}
