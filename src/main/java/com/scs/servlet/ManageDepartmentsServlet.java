package com.scs.servlet;

import com.scs.dao.ComplaintDAO;
import com.scs.dao.DepartmentDAO;
import com.scs.model.Department;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/departments")
public class ManageDepartmentsServlet extends HttpServlet {

    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final ComplaintDAO  complaintDAO  = new ComplaintDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Department> departments = departmentDAO.findAll();
        Map<Long, Long> loadByDept = new LinkedHashMap<>();
        for (Department d : departments) {
            loadByDept.put(d.getId(), complaintDAO.countByDepartment(d.getId()));
        }
        req.setAttribute("departments", departments);
        req.setAttribute("loadByDept", loadByDept);
        req.getRequestDispatcher("/jsp/admin/departments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        if ("create".equals(action)) {
            String name = trim(req.getParameter("name"));
            String description = trim(req.getParameter("description"));
            if (name == null || name.isEmpty()) {
                session.setAttribute("flash", "Department name is required.");
            } else if (departmentDAO.findByName(name) != null) {
                session.setAttribute("flash", "A department with that name already exists.");
            } else {
                departmentDAO.save(new Department(name, description));
                session.setAttribute("flash", "Department created.");
            }
        } else if ("update".equals(action)) {
            Long id = parseLong(req.getParameter("id"));
            if (id != null) {
                Department d = departmentDAO.findById(id);
                if (d != null) {
                    d.setName(trim(req.getParameter("name")));
                    d.setDescription(trim(req.getParameter("description")));
                    departmentDAO.update(d);
                    session.setAttribute("flash", "Department updated.");
                }
            }
        } else if ("delete".equals(action)) {
            Long id = parseLong(req.getParameter("id"));
            if (id != null) {
                long load = complaintDAO.countByDepartment(id);
                if (load > 0) {
                    session.setAttribute("flash",
                            "Cannot delete: " + load + " complaints are still assigned to this department.");
                } else {
                    departmentDAO.delete(id);
                    session.setAttribute("flash", "Department deleted.");
                }
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/departments");
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static Long parseLong(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
    }
}
