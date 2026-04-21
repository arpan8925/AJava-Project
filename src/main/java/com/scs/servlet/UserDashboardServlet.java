package com.scs.servlet;

import com.scs.dao.ComplaintDAO;
import com.scs.dao.NotificationDAO;
import com.scs.model.Complaint;
import com.scs.model.Complaint.Status;
import com.scs.model.Notification;
import com.scs.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    private final ComplaintDAO     complaintDAO    = new ComplaintDAO();
    private final NotificationDAO  notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedInUser");

        List<Complaint> mine = complaintDAO.findByUserId(user.getId());
        int total = mine.size(), pending = 0, inProgress = 0, resolved = 0, emergencies = 0;
        for (Complaint c : mine) {
            Status s = c.getStatus();
            if (s == Status.PENDING || s == Status.ASSIGNED) pending++;
            else if (s == Status.IN_PROGRESS) inProgress++;
            else if (s == Status.RESOLVED || s == Status.CLOSED) resolved++;
            if (Boolean.TRUE.equals(c.getIsEmergency())) emergencies++;
        }

        List<Complaint> recent = mine.size() > 5 ? mine.subList(0, 5) : mine;
        List<Notification> notifications = notificationDAO.findByUserId(user.getId());
        if (notifications.size() > 10) notifications = notifications.subList(0, 10);

        req.setAttribute("totalCount",       total);
        req.setAttribute("pendingCount",     pending);
        req.setAttribute("inProgressCount",  inProgress);
        req.setAttribute("resolvedCount",    resolved);
        req.setAttribute("emergencyCount",   emergencies);
        req.setAttribute("recentComplaints", recent);
        req.setAttribute("notifications",    notifications);
        req.setAttribute("unreadCount",      notificationDAO.countUnread(user.getId()));

        req.getRequestDispatcher("/jsp/user/dashboard.jsp").forward(req, resp);
    }
}
