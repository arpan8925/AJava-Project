package com.scs.servlet;

import com.google.gson.Gson;
import com.scs.dao.ComplaintDAO;
import com.scs.util.PredictiveAnalytics;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/analytics")
public class AnalyticsServlet extends HttpServlet {

    private static final int TREND_DAYS = 60;

    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -(TREND_DAYS - 1));
        zeroTime(cal);
        Date since = cal.getTime();

        List<Date> dates = complaintDAO.findCreatedAtSince(since);

        // Weekly bucket for trend over 60 days → 9 buckets
        SimpleDateFormat weekLabel = new SimpleDateFormat("MMM dd");
        List<String> labels = new ArrayList<>();
        List<Integer> series = new ArrayList<>();
        Calendar walker = (Calendar) cal.clone();
        for (int w = 0; w < 9; w++) {
            Date start = walker.getTime();
            walker.add(Calendar.DAY_OF_MONTH, 7);
            Date end = walker.getTime();
            int count = 0;
            for (Date d : dates) if (!d.before(start) && d.before(end)) count++;
            labels.add(weekLabel.format(start));
            series.add(count);
        }

        // Priority distribution
        Map<String, Long> priorityMap = new LinkedHashMap<>();
        for (Object[] row : complaintDAO.countByPriority()) {
            priorityMap.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        }

        // Department distribution
        Map<String, Long> deptMap = new LinkedHashMap<>();
        for (Object[] row : complaintDAO.countByDepartmentGrouped()) {
            deptMap.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        }

        // Status distribution via countFiltered
        Map<String, Long> statusMap = new LinkedHashMap<>();
        for (com.scs.model.Complaint.Status s : com.scs.model.Complaint.Status.values()) {
            statusMap.put(s.name(), complaintDAO.countByStatus(s));
        }

        // Forecast (recent 30 vs prior 30)
        Calendar c30 = Calendar.getInstance(); c30.add(Calendar.DAY_OF_MONTH, -30); zeroTime(c30);
        long recent30 = 0, prior30 = 0;
        for (Date d : dates) {
            if (d.after(c30.getTime())) recent30++;
            else prior30++;
        }
        PredictiveAnalytics.Forecast forecast =
                PredictiveAnalytics.forecastWithTrend(recent30, 30, prior30, 30);

        req.setAttribute("trendLabelsJson", gson.toJson(labels));
        req.setAttribute("trendSeriesJson", gson.toJson(series));
        req.setAttribute("priorityJson",    gson.toJson(priorityMap));
        req.setAttribute("departmentsJson", gson.toJson(deptMap));
        req.setAttribute("statusJson",      gson.toJson(statusMap));
        req.setAttribute("forecast",        forecast);
        req.setAttribute("totalRange",      dates.size());
        req.setAttribute("rangeDays",       TREND_DAYS);

        req.getRequestDispatcher("/jsp/admin/analytics.jsp").forward(req, resp);
    }

    private static void zeroTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }
}
