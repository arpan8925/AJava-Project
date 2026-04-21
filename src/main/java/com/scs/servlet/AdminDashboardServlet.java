package com.scs.servlet;

import com.google.gson.Gson;
import com.scs.dao.ComplaintDAO;
import com.scs.dao.DepartmentDAO;
import com.scs.dao.UserDAO;
import com.scs.model.Complaint;
import com.scs.model.User;
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
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private static final int TREND_DAYS = 30;

    private final ComplaintDAO  complaintDAO  = new ComplaintDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final UserDAO       userDAO       = new UserDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long total      = complaintDAO.countAll();
        long pending    = complaintDAO.countByStatus(Complaint.Status.PENDING);
        long assigned   = complaintDAO.countByStatus(Complaint.Status.ASSIGNED);
        long inProgress = complaintDAO.countByStatus(Complaint.Status.IN_PROGRESS);
        long resolved   = complaintDAO.countByStatus(Complaint.Status.RESOLVED);
        long emergencies = complaintDAO.findEmergencies().size();

        long citizens = userDAO.countByRole(User.Role.CITIZEN);
        long officers = userDAO.countByRole(User.Role.OFFICER);
        long departments = departmentDAO.findAll().size();

        List<Complaint> recent = complaintDAO.findAll(1, 10);

        // Trend: group complaint.createdAt timestamps into N daily buckets
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -(TREND_DAYS - 1));
        zeroTime(cal);
        Date since = cal.getTime();
        List<Date> dates = complaintDAO.findCreatedAtSince(since);

        SimpleDateFormat labelFmt = new SimpleDateFormat("MM-dd");
        labelFmt.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat keyFmt = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Integer> bucket = new LinkedHashMap<>();
        List<String> labels = new ArrayList<>(TREND_DAYS);
        Calendar walk = (Calendar) cal.clone();
        for (int i = 0; i < TREND_DAYS; i++) {
            String key = keyFmt.format(walk.getTime());
            bucket.put(key, 0);
            labels.add(labelFmt.format(walk.getTime()));
            walk.add(Calendar.DAY_OF_MONTH, 1);
        }
        for (Date d : dates) {
            String key = keyFmt.format(d);
            if (bucket.containsKey(key)) bucket.put(key, bucket.get(key) + 1);
        }
        List<Integer> trendSeries = new ArrayList<>(bucket.values());

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

        // Top 5 locations
        Map<String, Long> locMap = new LinkedHashMap<>();
        int count = 0;
        for (Object[] row : complaintDAO.countByLocation()) {
            if (count++ >= 5) break;
            locMap.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        }

        // Forecast
        long recent14Days = 0;
        long prior14Days  = 0;
        Calendar c14 = Calendar.getInstance(); c14.add(Calendar.DAY_OF_MONTH, -14); zeroTime(c14);
        Calendar c28 = Calendar.getInstance(); c28.add(Calendar.DAY_OF_MONTH, -28); zeroTime(c28);
        for (Date d : dates) {
            if (d.after(c14.getTime())) recent14Days++;
            else if (d.after(c28.getTime())) prior14Days++;
        }
        PredictiveAnalytics.Forecast forecast =
                PredictiveAnalytics.forecastWithTrend(recent14Days, 14, prior14Days, 14);

        req.setAttribute("totalCount",       total);
        req.setAttribute("pendingCount",     pending);
        req.setAttribute("assignedCount",    assigned);
        req.setAttribute("inProgressCount",  inProgress);
        req.setAttribute("resolvedCount",    resolved);
        req.setAttribute("emergencyCount",   emergencies);
        req.setAttribute("citizenCount",     citizens);
        req.setAttribute("officerCount",     officers);
        req.setAttribute("departmentCount",  departments);
        req.setAttribute("recentComplaints", recent);
        req.setAttribute("forecast",         forecast);

        req.setAttribute("trendLabelsJson",   gson.toJson(labels));
        req.setAttribute("trendSeriesJson",   gson.toJson(trendSeries));
        req.setAttribute("priorityJson",      gson.toJson(priorityMap));
        req.setAttribute("departmentsJson",   gson.toJson(deptMap));
        req.setAttribute("locationsJson",     gson.toJson(locMap));

        req.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(req, resp);
    }

    private static void zeroTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }
}
