package com.scs.servlet;

import com.scs.dao.ComplaintDAO;
import com.scs.dao.ComplaintReplyDAO;
import com.scs.dao.NotificationDAO;
import com.scs.dao.UserDAO;
import com.scs.model.Complaint;
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

@WebServlet({"/user/reply", "/officer/reply"})
public class ComplaintReplyServlet extends HttpServlet {

    private final ComplaintDAO      complaintDAO   = new ComplaintDAO();
    private final ComplaintReplyDAO replyDAO       = new ComplaintReplyDAO();
    private final NotificationDAO   notificationDAO = new NotificationDAO();
    private final UserDAO           userDAO        = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User author = (User) session.getAttribute("loggedInUser");

        String idParam = req.getParameter("complaintId");
        String message = req.getParameter("message");

        if (idParam == null || message == null || message.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/user/my-complaints");
            return;
        }

        long id;
        try { id = Long.parseLong(idParam); }
        catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/user/my-complaints");
            return;
        }

        Complaint c = complaintDAO.findById(id);
        if (c == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        boolean isOwner   = c.getUser() != null && c.getUser().getId().equals(author.getId());
        boolean isOfficer = author.getRole() == User.Role.OFFICER;
        boolean isAdmin   = author.getRole() == User.Role.ADMIN;

        if (!isOwner && !isOfficer && !isAdmin) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.getRequestDispatcher("/jsp/error/access-denied.jsp").forward(req, resp);
            return;
        }

        User authorRef = userDAO.findById(author.getId());
        replyDAO.save(new ComplaintReply(c, authorRef, message.trim()));

        // Notify the other party
        if (isOwner && c.getDepartment() != null) {
            // Future: notify officers of this department
        } else if ((isOfficer || isAdmin) && c.getUser() != null) {
            notificationDAO.save(new Notification(c.getUser(),
                    "New reply on your complaint #" + id + "."));
        }

        String redirect;
        if (author.getRole() == User.Role.CITIZEN) {
            redirect = "/user/track-complaint?id=" + id;
        } else if (author.getRole() == User.Role.OFFICER) {
            redirect = "/officer/resolve?id=" + id;
        } else {
            redirect = "/admin/complaints";
        }
        resp.sendRedirect(req.getContextPath() + redirect);
    }
}
