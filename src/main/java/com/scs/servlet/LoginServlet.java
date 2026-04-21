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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        HttpSession flashSession = req.getSession(false);
        if (flashSession != null) {
            Object flash = flashSession.getAttribute("flash");
            if (flash != null) {
                req.setAttribute("flash", flash);
                flashSession.removeAttribute("flash");
            }
        }
        req.getRequestDispatcher("/jsp/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/jsp/auth/login.jsp").forward(req, resp);
            return;
        }

        User user = userDAO.findByEmail(email);
        if (user == null || !Boolean.TRUE.equals(user.getIsActive())
                || !PasswordUtil.verify(password, user.getPassword())) {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/jsp/auth/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("loggedInUser", user);

        resp.sendRedirect(req.getContextPath() + roleLanding(user.getRole()));
    }

    private static String roleLanding(User.Role role) {
        switch (role) {
            case ADMIN:   return "/admin/dashboard";
            case OFFICER: return "/officer/dashboard";
            case CITIZEN:
            default:      return "/user/dashboard";
        }
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
}
