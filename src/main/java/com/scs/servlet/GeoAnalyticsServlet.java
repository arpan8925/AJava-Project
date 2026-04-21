package com.scs.servlet;

import com.google.gson.Gson;
import com.scs.dao.ComplaintDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/geo-analytics")
public class GeoAnalyticsServlet extends HttpServlet {

    private static final int TOP_N = 10;

    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Object[]> rows = complaintDAO.countByLocation();

        Map<String, Long> allLocations = new LinkedHashMap<>();
        for (Object[] row : rows) {
            allLocations.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        }

        Map<String, Long> topN = new LinkedHashMap<>();
        int i = 0;
        for (Map.Entry<String, Long> e : allLocations.entrySet()) {
            if (i++ >= TOP_N) break;
            topN.put(e.getKey(), e.getValue());
        }

        req.setAttribute("allLocations", allLocations);
        req.setAttribute("topLocations", topN);
        req.setAttribute("locationsJson", gson.toJson(topN));
        req.getRequestDispatcher("/jsp/admin/geo-analytics.jsp").forward(req, resp);
    }
}
