package com.scs.util;

public final class PredictiveAnalytics {

    private PredictiveAnalytics() {}

    public static Forecast forecastNext30Days(long totalComplaints, int daysInRange) {
        if (daysInRange <= 0) {
            return new Forecast(0.0, 0, Trend.FLAT);
        }
        double dailyRate = (double) totalComplaints / daysInRange;
        int predicted = (int) Math.round(dailyRate * 30);
        return new Forecast(dailyRate, predicted, Trend.FLAT);
    }

    public static Forecast forecastWithTrend(long complaintsRecentWindow, int recentDays,
                                             long complaintsPriorWindow,  int priorDays) {
        if (recentDays <= 0) return new Forecast(0.0, 0, Trend.FLAT);
        double recentRate = (double) complaintsRecentWindow / recentDays;
        double priorRate  = priorDays > 0 ? (double) complaintsPriorWindow / priorDays : recentRate;

        Trend trend;
        if (priorRate == 0 && recentRate == 0)       trend = Trend.FLAT;
        else if (priorRate == 0)                     trend = Trend.UP;
        else {
            double deltaPct = (recentRate - priorRate) / priorRate;
            if (deltaPct > 0.10)       trend = Trend.UP;
            else if (deltaPct < -0.10) trend = Trend.DOWN;
            else                       trend = Trend.FLAT;
        }

        int predicted = (int) Math.round(recentRate * 30);
        return new Forecast(recentRate, predicted, trend);
    }

    public enum Trend { UP, DOWN, FLAT }

    public static final class Forecast {
        public final double dailyRate;
        public final int predictedNext30Days;
        public final Trend trend;
        public Forecast(double dailyRate, int predictedNext30Days, Trend trend) {
            this.dailyRate = dailyRate;
            this.predictedNext30Days = predictedNext30Days;
            this.trend = trend;
        }
        public double getDailyRate()          { return dailyRate; }
        public int    getPredictedNext30Days() { return predictedNext30Days; }
        public Trend  getTrend()               { return trend; }
        @Override public String toString() {
            return "Forecast{" + String.format("%.2f/day", dailyRate)
                    + ", 30d=" + predictedNext30Days + ", trend=" + trend + "}";
        }
    }
}
