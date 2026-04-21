package com.scs.servlet;

import com.scs.dao.ComplaintDAO;
import com.scs.model.Complaint;
import com.scs.model.Complaint.Status;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/officer/complaints")
public class AssignedComplaintsServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String filter = req.getParameter("status");
        if (filter == null || filter.isEmpty()) filter = "ACTIVE";

        List<Complaint> complaints;
        if ("ACTIVE".equalsIgnoreCase(filter)) {
            complaints = new ArrayList<>();
            complaints.addAll(complaintDAO.findByStatus(Status.ASSIGNED));
            complaints.addAll(complaintDAO.findByStatus(Status.IN_PROGRESS));
        } else if ("ALL".equalsIgnoreCase(filter)) {
            complaints = complaintDAO.findAll(1, 200);
        } else {
            try {
                complaints = complaintDAO.findByStatus(Status.valueOf(filter.toUpperCase()));
            } catch (IllegalArgumentException e) {
                complaints = complaintDAO.findByStatus(Status.ASSIGNED);
            }
        }

        req.setAttribute("complaints",   complaints);
        req.setAttribute("statusFilter", filter.toUpperCase());
        req.getRequestDispatcher("/jsp/officer/complaints.jsp").forward(req, resp);
    }
}
