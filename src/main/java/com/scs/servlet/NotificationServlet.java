package com.scs.servlet;

import com.google.gson.Gson;
import com.scs.dao.NotificationDAO;
import com.scs.model.Notification;
import com.scs.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/notifications")
public class NotificationServlet extends HttpServlet {

    private final NotificationDAO dao = new NotificationDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedInUser");

        String unreadOnly = req.getParameter("unread");
        List<Notification> list = "true".equals(unreadOnly)
                ? dao.findUnreadByUserId(user.getId())
                : dao.findByUserId(user.getId());

        long unreadCount = dao.countUnread(user.getId());

        List<Map<String, Object>> out = new ArrayList<>(list.size());
        for (Notification n : list) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", n.getId());
            m.put("message", n.getMessage());
            m.put("isRead", Boolean.TRUE.equals(n.getIsRead()));
            Date created = n.getCreatedAt();
            m.put("createdAt", created == null ? null : created.getTime());
            out.add(m);
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("unreadCount", unreadCount);
        body.put("notifications", out);

        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter w = resp.getWriter()) {
            w.write(gson.toJson(body));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            dao.markAsRead(Long.parseLong(idParam));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.setContentType("application/json");
        resp.getWriter().write("{\"ok\":true}");
    }
}
