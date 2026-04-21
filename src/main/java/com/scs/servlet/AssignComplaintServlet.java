package com.scs.servlet;

import com.scs.dao.AnalyticsLogDAO;
import com.scs.dao.ComplaintDAO;
import com.scs.dao.DepartmentDAO;
import com.scs.dao.NotificationDAO;
import com.scs.dao.UserDAO;
import com.scs.model.AnalyticsLog;
import com.scs.model.Complaint;
import com.scs.model.Department;
import com.scs.model.Notification;
import com.scs.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/assign")
public class AssignComplaintServlet extends HttpServlet {

    private final ComplaintDAO    complaintDAO    = new ComplaintDAO();
    private final DepartmentDAO   departmentDAO   = new DepartmentDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final UserDAO         userDAO         = new UserDAO();
    private final AnalyticsLogDAO analyticsDAO    = new AnalyticsLogDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Long complaintId  = parseLong(req.getParameter("complaintId"));
        Long departmentId = parseLong(req.getParameter("departmentId"));

        if (complaintId == null || departmentId == null) {
            session.setAttribute("flash", "Missing complaint or department id.");
            resp.sendRedirect(redirectBack(req));
            return;
        }

        Complaint c = complaintDAO.findById(complaintId);
        Department d = departmentDAO.findById(departmentId);
        if (c == null || d == null) {
            session.setAttribute("flash", "Complaint or department not found.");
            resp.sendRedirect(redirectBack(req));
            return;
        }

        c.setDepartment(d);
        if (c.getStatus() == Complaint.Status.PENDING) {
            c.setStatus(Complaint.Status.ASSIGNED);
        }
        complaintDAO.update(c);

        // Notify officer(s) of that department. Schema has no officer/department link,
        // so we notify all officers. Phase 7 can tighten this if wired up.
        for (User officer : userDAO.findByRole(User.Role.OFFICER)) {
            notificationDAO.save(new Notification(officer,
                    "Complaint #" + complaintId + " assigned to " + d.getName() + "."));
        }
        if (c.getUser() != null) {
            notificationDAO.save(new Notification(c.getUser(),
                    "Your complaint #" + complaintId + " was assigned to " + d.getName() + "."));
        }
        analyticsDAO.save(new AnalyticsLog("COMPLAINT_ASSIGNED", complaintId, d.getId(), c.getLocation()));

        session.setAttribute("flash", "Complaint #" + complaintId + " assigned to " + d.getName() + ".");
        resp.sendRedirect(redirectBack(req));
    }

    private static String redirectBack(HttpServletRequest req) {
        String back = req.getParameter("back");
        if (back != null && back.startsWith("/")) return req.getContextPath() + back;
        return req.getContextPath() + "/admin/complaints";
    }

    private static Long parseLong(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
    }
}
