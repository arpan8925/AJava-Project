package com.scs.servlet;

import com.google.gson.Gson;
import com.scs.util.EmergencyDetector;
import com.scs.util.SentimentAnalyzer;
import com.scs.util.SmartRecommender;
import com.scs.util.TagGenerator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/suggest")
public class SmartSuggestServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String title = param(req.getParameter("title"));
        String description = param(req.getParameter("description"));
        String category = param(req.getParameter("category"));

        List<String> tags = TagGenerator.generateTags(title, description);
        EmergencyDetector.Result emergency = EmergencyDetector.detect(title, description);
        SentimentAnalyzer.Result sentiment = SentimentAnalyzer.analyze(title + " " + description);
        List<String> recommendations = SmartRecommender.recommend(title, description, category);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tags", tags);
        body.put("isEmergency", emergency.isEmergency);
        body.put("emergencyKeywords", emergency.matched);
        body.put("sentiment", sentiment.sentiment);
        body.put("priority", sentiment.priority.name());
        body.put("recommendations", recommendations);

        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter w = resp.getWriter()) {
            w.write(gson.toJson(body));
        }
    }

    private static String param(String s) { return s == null ? "" : s; }
}
