package com.scs.servlet;

import com.scs.dao.UserDAO;
import com.scs.model.User;
import com.scs.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Pattern EMAIL_RE = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = trim(req.getParameter("name"));
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");
        String phone = trim(req.getParameter("phone"));
        String area = trim(req.getParameter("area"));
        String roleParam = trim(req.getParameter("role"));

        String error = validate(name, email, password, roleParam);
        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/jsp/auth/register.jsp").forward(req, resp);
            return;
        }

        if (userDAO.findByEmail(email) != null) {
            req.setAttribute("error", "An account with that email already exists.");
            req.getRequestDispatcher("/jsp/auth/register.jsp").forward(req, resp);
            return;
        }

        User.Role role = "OFFICER".equalsIgnoreCase(roleParam) ? User.Role.OFFICER : User.Role.CITIZEN;

        User u = new User(name, email, PasswordUtil.hash(password), role);
        u.setPhone(phone);
        u.setArea(area);
        u.setIsActive(Boolean.TRUE);
        userDAO.save(u);

        HttpSession session = req.getSession(true);
        session.setAttribute("flash", "Account created. Please sign in.");
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private static String validate(String name, String email, String password, String role) {
        if (name == null || name.length() < 2)   return "Name must be at least 2 characters.";
        if (email == null || !EMAIL_RE.matcher(email).matches()) return "Please enter a valid email.";
        if (password == null || password.length() < 6) return "Password must be at least 6 characters.";
        if (role != null && !role.isEmpty()
                && !role.equalsIgnoreCase("CITIZEN")
                && !role.equalsIgnoreCase("OFFICER")) {
            return "Invalid role selection.";
        }
        return null;
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
}
