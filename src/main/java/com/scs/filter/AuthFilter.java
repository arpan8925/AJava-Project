package com.scs.filter;

import com.scs.model.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/user/*", "/admin/*", "/officer/*", "/api/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String ctx  = req.getContextPath();
        String path = req.getRequestURI().substring(ctx.length());

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("loggedInUser");

        if (user == null) {
            if (path.startsWith("/api/")) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\":\"not authenticated\"}");
            } else {
                resp.sendRedirect(ctx + "/login");
            }
            return;
        }

        if (path.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        User.Role role = user.getRole();
        boolean allowed =
                   (path.startsWith("/admin/")   && role == User.Role.ADMIN)
                || (path.startsWith("/officer/") && role == User.Role.OFFICER)
                || (path.startsWith("/user/")    && role == User.Role.CITIZEN);

        if (!allowed) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.getRequestDispatcher("/jsp/error/access-denied.jsp").forward(req, resp);
            return;
        }

        chain.doFilter(request, response);
    }
}
