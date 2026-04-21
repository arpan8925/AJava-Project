package com.scs.servlet;

import com.scs.dao.ComplaintDAO;
import com.scs.model.Complaint;
import com.scs.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/user/my-complaints")
public class MyComplaintsServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedInUser");

        List<Complaint> all = complaintDAO.findByUserId(user.getId());
        String statusFilter = req.getParameter("status");

        List<Complaint> filtered;
        if (statusFilter != null && !statusFilter.isEmpty() && !"ALL".equalsIgnoreCase(statusFilter)) {
            filtered = new ArrayList<>();
            for (Complaint c : all) {
                if (c.getStatus() != null && c.getStatus().name().equalsIgnoreCase(statusFilter)) {
                    filtered.add(c);
                }
            }
        } else {
            filtered = all;
        }

        req.setAttribute("complaints", filtered);
        req.setAttribute("currentFilter", statusFilter == null ? "ALL" : statusFilter.toUpperCase());
        req.setAttribute("totalCount", all.size());
        req.getRequestDispatcher("/jsp/user/my-complaints.jsp").forward(req, resp);
    }
}
