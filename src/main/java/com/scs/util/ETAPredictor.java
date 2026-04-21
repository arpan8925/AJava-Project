package com.scs.util;

import com.scs.model.Complaint.Priority;

public final class ETAPredictor {

    public static final int CRITICAL_HOURS = 4;
    public static final int HIGH_HOURS     = 24;
    public static final int MEDIUM_HOURS   = 72;
    public static final int LOW_HOURS      = 168;

    public static final int HIGH_LOAD_THRESHOLD = 20;
    public static final double HIGH_LOAD_MULTIPLIER = 1.5;
    public static final double REPEAT_USER_MULTIPLIER = 0.8;

    private ETAPredictor() {}

    public static int predictETA(Priority priority, int deptOpenCount, boolean isRepeatUser) {
        int base = baseHours(priority);
        double adjusted = base;
        if (deptOpenCount > HIGH_LOAD_THRESHOLD) adjusted *= HIGH_LOAD_MULTIPLIER;
        if (isRepeatUser) adjusted *= REPEAT_USER_MULTIPLIER;
        return (int) Math.round(adjusted);
    }

    private static int baseHours(Priority p) {
        if (p == null) return MEDIUM_HOURS;
        switch (p) {
            case CRITICAL: return CRITICAL_HOURS;
            case HIGH:     return HIGH_HOURS;
            case MEDIUM:   return MEDIUM_HOURS;
            case LOW:      return LOW_HOURS;
            default:       return MEDIUM_HOURS;
        }
    }
}
