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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/officer/dashboard")
public class OfficerDashboardServlet extends HttpServlet {

    private final ComplaintDAO    complaintDAO    = new ComplaintDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User officer = (User) session.getAttribute("loggedInUser");

        long assigned   = complaintDAO.countByStatus(Status.ASSIGNED);
        long inProgress = complaintDAO.countByStatus(Status.IN_PROGRESS);
        long resolved   = complaintDAO.countByStatus(Status.RESOLVED);
        List<Complaint> emergencies = complaintDAO.findEmergencies();

        List<Complaint> assignedList   = complaintDAO.findByStatus(Status.ASSIGNED);
        List<Complaint> inProgressList = complaintDAO.findByStatus(Status.IN_PROGRESS);
        List<Complaint> activeQueue    = new ArrayList<>(assignedList.size() + inProgressList.size());
        activeQueue.addAll(assignedList);
        activeQueue.addAll(inProgressList);

        List<Complaint> activeEmergencies = new ArrayList<>();
        for (Complaint c : emergencies) {
            if (c.getStatus() == Status.ASSIGNED || c.getStatus() == Status.IN_PROGRESS
                    || c.getStatus() == Status.PENDING) {
                activeEmergencies.add(c);
            }
        }

        List<Complaint> recent = activeQueue.size() > 10 ? activeQueue.subList(0, 10) : activeQueue;
        long unread = notificationDAO.countUnread(officer.getId());

        req.setAttribute("assignedCount",      assigned);
        req.setAttribute("inProgressCount",    inProgress);
        req.setAttribute("resolvedCount",      resolved);
        req.setAttribute("emergencyCount",     activeEmergencies.size());
        req.setAttribute("activeEmergencies",  activeEmergencies);
        req.setAttribute("recentAssigned",     recent);
        req.setAttribute("unreadNotifications", unread);

        req.getRequestDispatcher("/jsp/officer/dashboard.jsp").forward(req, resp);
    }
}
