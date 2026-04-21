package com.scs.servlet;

import com.scs.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardRouterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loggedInUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        switch (user.getRole()) {
            case ADMIN:   resp.sendRedirect(req.getContextPath() + "/admin/dashboard");   return;
            case OFFICER: resp.sendRedirect(req.getContextPath() + "/officer/dashboard"); return;
            case CITIZEN:
            default:      resp.sendRedirect(req.getContextPath() + "/user/dashboard");    return;
        }
    }
}
