package com.scs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class EmergencyDetector {

    public static final List<String> KEYWORDS = Collections.unmodifiableList(Arrays.asList(
            "fire", "flood", "accident", "collapse", "explosion", "danger", "death",
            "electrocution", "gas leak", "emergency", "drowning", "riot", "earthquake",
            "tornado", "bomb"
    ));

    private EmergencyDetector() {}

    public static Result detect(String title, String description) {
        String haystack = ((title == null ? "" : title) + " "
                        + (description == null ? "" : description)).toLowerCase();

        Set<String> matches = new LinkedHashSet<>();
        for (String keyword : KEYWORDS) {
            if (containsWholeWord(haystack, keyword)) matches.add(keyword);
        }
        return new Result(!matches.isEmpty(), new ArrayList<>(matches));
    }

    private static boolean containsWholeWord(String haystack, String keyword) {
        if (keyword.contains(" ")) {
            return haystack.contains(keyword);
        }
        int idx = 0;
        while ((idx = haystack.indexOf(keyword, idx)) != -1) {
            boolean leftOk  = (idx == 0) || !isWordChar(haystack.charAt(idx - 1));
            int end = idx + keyword.length();
            boolean rightOk = (end == haystack.length()) || !isWordChar(haystack.charAt(end));
            if (leftOk && rightOk) return true;
            idx += keyword.length();
        }
        return false;
    }

    private static boolean isWordChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    public static final class Result {
        public final boolean isEmergency;
        public final List<String> matched;
        public Result(boolean isEmergency, List<String> matched) {
            this.isEmergency = isEmergency;
            this.matched = Collections.unmodifiableList(matched);
        }
        @Override public String toString() {
            return "EmergencyResult{" + isEmergency + ", matched=" + matched + "}";
        }
    }
}
