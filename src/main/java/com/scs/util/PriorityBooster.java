package com.scs.util;

import com.scs.model.Complaint.Priority;

public final class PriorityBooster {

    private PriorityBooster() {}

    public static Priority boost(Priority current, int recentCount, int unresolvedOlderThan7Days) {
        if (current == null) current = Priority.MEDIUM;
        Priority result = current;

        if (recentCount >= 5 && result == Priority.MEDIUM) {
            result = Priority.HIGH;
        } else if (recentCount >= 3 && result == Priority.LOW) {
            result = Priority.MEDIUM;
        }

        if (unresolvedOlderThan7Days > 0) {
            result = oneLevelUp(result);
        }

        return result;
    }

    public static Priority oneLevelUp(Priority p) {
        if (p == null) return Priority.MEDIUM;
        switch (p) {
            case LOW:      return Priority.MEDIUM;
            case MEDIUM:   return Priority.HIGH;
            case HIGH:     return Priority.CRITICAL;
            case CRITICAL: return Priority.CRITICAL;
            default:       return p;
        }
    }
}
