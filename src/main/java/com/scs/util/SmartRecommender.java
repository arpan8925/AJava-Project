package com.scs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SmartRecommender {

    private static final Map<String, List<String>> CATEGORIES = new LinkedHashMap<>();
    private static final Map<String, String[]> KEYWORDS = new LinkedHashMap<>();

    static {
        put("water",
                new String[]{"water", "pipe", "supply", "leak", "leakage", "tap", "tanker", "plumb"},
                "Contact Water Supply dept at ext. 201",
                "Check area-wide outage status on the citizen portal",
                "Request a temporary tanker service if outage exceeds 24 hrs");

        put("road",
                new String[]{"road", "pothole", "traffic", "divider", "footpath", "signal", "lane"},
                "Road repair team is dispatched within 48 hours for reported potholes",
                "Use alternate routes via the municipal traffic advisory",
                "Report pothole depth and GPS coordinates for faster triage");

        put("electricity",
                new String[]{"electric", "electricity", "power", "outage", "current", "transformer",
                             "streetlight", "wire", "voltage"},
                "Check scheduled maintenance calendar for planned outages",
                "Report to electricity board helpline: 1800-XXX-XXXX",
                "Avoid touching exposed wires; keep 3 metre distance");

        put("sanitation",
                new String[]{"garbage", "waste", "sanitation", "sewer", "sewage", "drain", "drainage",
                             "dump", "trash"},
                "Garbage collection runs Mon / Wed / Fri in most wards",
                "Raise a missed-pickup ticket for same-day collection",
                "Separate wet and dry waste in labelled bins");

        put("safety",
                new String[]{"fire", "flood", "accident", "collapse", "explosion", "emergency",
                             "danger", "dangerous", "unsafe", "hazard", "riot", "bomb"},
                "Emergency services have been auto-notified",
                "Evacuate the area and keep a 50 metre perimeter",
                "Dial 112 for immediate life-threatening situations");

        put("parks",
                new String[]{"park", "garden", "playground", "swing", "tree", "bench", "lawn"},
                "Parks & Recreation dept inspects weekly on Wednesdays",
                "Report broken equipment with photo for fastest resolution",
                "Volunteer clean-up drives happen on the first Saturday of every month");

        put("noise",
                new String[]{"noise", "loud", "music", "horn", "honk"},
                "Noise complaints after 10pm are routed to local police",
                "Submit decibel reading if available (any phone app works)",
                "Log repeat incidents by date/time for pattern escalation");

        put("stray",
                new String[]{"stray", "dog", "animal", "cattle", "monkey"},
                "Animal control unit responds within 24 hours",
                "Do not attempt to approach or feed aggressive strays",
                "Vaccinated strays are identified by ear-notch markings");

        put("building",
                new String[]{"building", "structure", "wall", "illegal", "encroach"},
                "Building & zoning dept investigates encroachment complaints",
                "Provide property survey number if known",
                "Demolition orders require 14-day notice under local code");

        put("health",
                new String[]{"mosquito", "dengue", "malaria", "fever", "disease", "epidemic"},
                "Fogging is scheduled in monsoon season (Jun-Sep)",
                "Remove standing water sources around your premises",
                "Report clusters of fever cases to the local PHC");
    }

    private static void put(String category, String[] keywords, String... solutions) {
        CATEGORIES.put(category, Collections.unmodifiableList(Arrays.asList(solutions)));
        KEYWORDS.put(category, keywords);
    }

    private SmartRecommender() {}

    public static List<String> recommend(String title, String description, String category) {
        String haystack = ((title == null ? "" : title) + " "
                        + (description == null ? "" : description)).toLowerCase();

        Set<String> matchedCategories = new LinkedHashSet<>();
        if (category != null) {
            String c = category.toLowerCase().trim();
            if (CATEGORIES.containsKey(c)) matchedCategories.add(c);
        }
        for (Map.Entry<String, String[]> e : KEYWORDS.entrySet()) {
            for (String kw : e.getValue()) {
                if (haystack.contains(kw)) {
                    matchedCategories.add(e.getKey());
                    break;
                }
            }
        }

        if (matchedCategories.isEmpty()) {
            return Collections.singletonList(
                    "Thank you for reporting this. Your complaint has been logged and will be reviewed by the relevant department.");
        }

        List<String> out = new ArrayList<>();
        for (String c : matchedCategories) {
            out.addAll(CATEGORIES.get(c));
            if (out.size() >= 5) break;
        }
        if (out.size() > 5) out = new ArrayList<>(out.subList(0, 5));
        return Collections.unmodifiableList(out);
    }

    public static Set<String> supportedCategories() {
        return Collections.unmodifiableSet(CATEGORIES.keySet());
    }
}
