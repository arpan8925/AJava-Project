package com.scs.servlet;

import com.scs.dao.ComplaintDAO;
import com.scs.dao.DepartmentDAO;
import com.scs.model.Complaint;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/complaints")
public class AdminComplaintsServlet extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    private final ComplaintDAO  complaintDAO  = new ComplaintDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String statusFilter   = blank(req.getParameter("status"));
        String priorityFilter = blank(req.getParameter("priority"));
        String deptParam      = blank(req.getParameter("dept"));
        boolean emergencyOnly = "true".equalsIgnoreCase(blank(req.getParameter("emergency")));

        Long deptId = null;
        if (deptParam != null) {
            try { deptId = Long.parseLong(deptParam); }
            catch (NumberFormatException ignored) { /* filter off */ }
        }

        int page = 1;
        try { page = Math.max(1, Integer.parseInt(blank(req.getParameter("page")) == null
                        ? "1" : req.getParameter("page"))); }
        catch (NumberFormatException ignored) {}

        long total = complaintDAO.countFiltered(statusFilter, priorityFilter, deptId,
                emergencyOnly ? Boolean.TRUE : null);
        int totalPages = (int) Math.max(1, Math.ceil((double) total / PAGE_SIZE));
        if (page > totalPages) page = totalPages;

        List<Complaint> complaints = complaintDAO.findFiltered(
                statusFilter, priorityFilter, deptId, emergencyOnly ? Boolean.TRUE : null,
                page, PAGE_SIZE);

        req.setAttribute("complaints",    complaints);
        req.setAttribute("total",         total);
        req.setAttribute("page",          page);
        req.setAttribute("totalPages",    totalPages);
        req.setAttribute("pageSize",      PAGE_SIZE);
        req.setAttribute("statusFilter",   statusFilter == null ? "" : statusFilter);
        req.setAttribute("priorityFilter", priorityFilter == null ? "" : priorityFilter);
        req.setAttribute("deptFilter",     deptId);
        req.setAttribute("emergencyOnly",  emergencyOnly);
        req.setAttribute("departments",    departmentDAO.findAll());

        req.getRequestDispatcher("/jsp/admin/complaints.jsp").forward(req, resp);
    }

    private static String blank(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }
}
