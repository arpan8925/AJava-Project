package com.scs.servlet;

import com.google.gson.Gson;
import com.scs.dao.ComplaintDAO;
import com.scs.model.Complaint;
import com.scs.util.StopWords;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/similar")
public class SimilarComplaintsServlet extends HttpServlet {

    private static final int MAX_RESULTS = 5;
    private static final int MAX_KEYWORDS = 4;
    private static final int MIN_KEYWORD_LEN = 4;

    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long excludeId = parseLong(req.getParameter("id"));
        String title = req.getParameter("title");
        String description = req.getParameter("description");

        if ((title == null || title.isEmpty()) && excludeId != null) {
            Complaint source = complaintDAO.findById(excludeId);
            if (source != null) {
                title = source.getTitle();
                description = source.getDescription();
            }
        }

        List<String> keywords = extractKeywords(title, description);
        List<Complaint> similar = keywords.isEmpty()
                ? new ArrayList<>()
                : complaintDAO.findSimilar(keywords, excludeId, MAX_RESULTS);

        List<Map<String, Object>> out = new ArrayList<>(similar.size());
        for (Complaint c : similar) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", c.getId());
            m.put("title", c.getTitle());
            m.put("status", c.getStatus() == null ? null : c.getStatus().name());
            m.put("priority", c.getPriority() == null ? null : c.getPriority().name());
            m.put("location", c.getLocation());
            Date d = c.getResolvedAt() != null ? c.getResolvedAt() : c.getCreatedAt();
            m.put("date", d == null ? null : d.getTime());
            out.add(m);
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("keywords", keywords);
        body.put("results", out);

        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter w = resp.getWriter()) {
            w.write(gson.toJson(body));
        }
    }

    private static List<String> extractKeywords(String title, String description) {
        String text = ((title == null ? "" : title) + " "
                    + (description == null ? "" : description)).toLowerCase();
        String[] tokens = text.split("[^a-z0-9]+");
        List<String> result = new ArrayList<>();
        for (String t : tokens) {
            if (t.length() < MIN_KEYWORD_LEN) continue;
            if (StopWords.isStopWord(t)) continue;
            if (!result.contains(t)) result.add(t);
            if (result.size() >= MAX_KEYWORDS) break;
        }
        return result;
    }

    private static Long parseLong(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
    }
}
