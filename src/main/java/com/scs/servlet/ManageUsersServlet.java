package com.scs.servlet;

import com.scs.dao.UserDAO;
import com.scs.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class ManageUsersServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String roleFilter = req.getParameter("role");
        List<User> users;
        if (roleFilter == null || roleFilter.isEmpty() || "ALL".equalsIgnoreCase(roleFilter)) {
            users = userDAO.findAll();
        } else {
            try {
                users = userDAO.findByRole(User.Role.valueOf(roleFilter.toUpperCase()));
            } catch (IllegalArgumentException e) {
                users = userDAO.findAll();
            }
        }
        req.setAttribute("users", users);
        req.setAttribute("roleFilter", roleFilter == null ? "ALL" : roleFilter.toUpperCase());
        req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User current = (User) session.getAttribute("loggedInUser");
        String action = req.getParameter("action");
        Long id = parseLong(req.getParameter("id"));

        if (id == null || action == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        User target = userDAO.findById(id);
        if (target == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        if (current != null && current.getId().equals(id)) {
            session.setAttribute("flash", "You cannot modify your own account from this page.");
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        if ("updateRole".equals(action)) {
            String newRole = req.getParameter("role");
            try {
                target.setRole(User.Role.valueOf(newRole));
                userDAO.update(target);
                session.setAttribute("flash", "Role updated for " + target.getName() + ".");
            } catch (IllegalArgumentException e) {
                session.setAttribute("flash", "Invalid role: " + newRole);
            }
        } else if ("toggleActive".equals(action)) {
            target.setIsActive(!Boolean.TRUE.equals(target.getIsActive()));
            userDAO.update(target);
            session.setAttribute("flash", (target.getIsActive() ? "Activated " : "Deactivated ") + target.getName() + ".");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private static Long parseLong(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
    }
}
