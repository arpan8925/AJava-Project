package com.scs.util;

import com.scs.util.PredictiveAnalytics.Forecast;
import com.scs.util.PredictiveAnalytics.Trend;
import org.junit.Test;

import static org.junit.Assert.*;

public class PredictiveAnalyticsTest {

    @Test
    public void dailyRateAndThirtyDayForecast() {
        Forecast f = PredictiveAnalytics.forecastNext30Days(60, 30);
        assertEquals(2.0, f.dailyRate, 0.001);
        assertEquals(60, f.predictedNext30Days);
    }

    @Test
    public void zeroDaysIsSafe() {
        Forecast f = PredictiveAnalytics.forecastNext30Days(10, 0);
        assertEquals(0.0, f.dailyRate, 0.001);
        assertEquals(0, f.predictedNext30Days);
    }

    @Test
    public void upwardTrendDetected() {
        Forecast f = PredictiveAnalytics.forecastWithTrend(40, 10, 20, 10);
        assertEquals(Trend.UP, f.trend);
    }

    @Test
    public void downwardTrendDetected() {
        Forecast f = PredictiveAnalytics.forecastWithTrend(10, 10, 40, 10);
        assertEquals(Trend.DOWN, f.trend);
    }

    @Test
    public void flatTrendWhenEqual() {
        Forecast f = PredictiveAnalytics.forecastWithTrend(20, 10, 20, 10);
        assertEquals(Trend.FLAT, f.trend);
    }

    @Test
    public void priorZeroButRecentNonZeroIsUp() {
        Forecast f = PredictiveAnalytics.forecastWithTrend(10, 10, 0, 10);
        assertEquals(Trend.UP, f.trend);
    }
}
