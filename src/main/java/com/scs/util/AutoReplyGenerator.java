package com.scs.util;

import com.scs.model.Complaint;
import com.scs.model.Complaint.Priority;

import java.util.HashMap;
import java.util.Map;

public final class AutoReplyGenerator {

    private static final Map<String, Map<Priority, String>> TEMPLATES = new HashMap<>();

    static {
        put("water", Priority.CRITICAL, "We have received your CRITICAL water-related complaint (ref #%d). An emergency crew has been dispatched.");
        put("water", Priority.HIGH,     "We have received your urgent water-related complaint (ref #%d). A team will be dispatched within 4 hours.");
        put("water", Priority.MEDIUM,   "Your water complaint (ref #%d) is logged. Expected resolution in %d hours.");
        put("water", Priority.LOW,      "Thank you for reporting the water issue (ref #%d). We'll reach you within %d hours.");

        put("road", Priority.CRITICAL,  "Your CRITICAL road hazard (ref #%d) has been escalated. Crew responding immediately.");
        put("road", Priority.HIGH,      "Your road complaint (ref #%d) is marked HIGH priority. Repair team arrives within 24 hours.");
        put("road", Priority.MEDIUM,    "Your road maintenance complaint (ref #%d) has been logged. Expected resolution within 7 days.");
        put("road", Priority.LOW,       "Thank you for reporting the road issue (ref #%d). We'll schedule it during the next maintenance cycle.");

        put("electricity", Priority.CRITICAL, "CRITICAL electricity hazard (ref #%d) detected. Power may be isolated for safety.");
        put("electricity", Priority.HIGH,     "Your electricity complaint (ref #%d) is urgent. Technicians will arrive within 4 hours.");
        put("electricity", Priority.MEDIUM,   "Your electricity complaint (ref #%d) is logged. Expected resolution in %d hours.");
        put("electricity", Priority.LOW,      "Thank you for reporting the electricity issue (ref #%d). We'll handle it in the next service window.");

        put("sanitation", Priority.CRITICAL, "CRITICAL sanitation issue (ref #%d) flagged. Special sanitation crew dispatched.");
        put("sanitation", Priority.HIGH,     "Your sanitation complaint (ref #%d) is urgent. Crew visits within 24 hours.");
        put("sanitation", Priority.MEDIUM,   "Your sanitation complaint (ref #%d) is logged. Expected pickup in %d hours.");
        put("sanitation", Priority.LOW,      "Thank you for the sanitation report (ref #%d). Scheduled for the next collection cycle.");

        put("safety", Priority.CRITICAL, "EMERGENCY complaint (ref #%d) received. Public Safety team and admins have been notified immediately. Please evacuate if necessary.");
        put("safety", Priority.HIGH,     "Your safety concern (ref #%d) is marked HIGH priority. Authorities will arrive as soon as possible.");
        put("safety", Priority.MEDIUM,   "Your safety complaint (ref #%d) is logged. Expected response in %d hours.");
        put("safety", Priority.LOW,      "Thank you for reporting the safety issue (ref #%d). We'll review and follow up.");

        put("parks", Priority.MEDIUM,    "Your parks complaint (ref #%d) has been logged. The Parks & Rec team inspects weekly.");
        put("parks", Priority.LOW,       "Thank you for the parks report (ref #%d). It will be reviewed during the next inspection.");
    }

    private static void put(String category, Priority priority, String template) {
        TEMPLATES.computeIfAbsent(category, k -> new HashMap<>()).put(priority, template);
    }

    private AutoReplyGenerator() {}

    public static String generateReply(Complaint c) {
        if (c == null) {
            return "Thank you for your complaint. It has been received.";
        }
        String cat = c.getCategory() == null ? "" : c.getCategory().toLowerCase();
        Priority p = c.getPriority() == null ? Priority.MEDIUM : c.getPriority();
        long id = c.getId() == null ? 0L : c.getId();
        int eta = c.getEtaHours() == null ? etaFallback(p) : c.getEtaHours();

        Map<Priority, String> byCat = TEMPLATES.get(cat);
        String template = (byCat != null) ? byCat.get(p) : null;
        if (template == null) {
            return String.format(
                    "Thank you for your complaint. Your reference ID is %d. Current estimated resolution: %d hours.",
                    id, eta);
        }

        if (template.contains("%d") && countFormatSpecifiers(template) == 2) {
            return String.format(template, id, eta);
        }
        return String.format(template, id);
    }

    private static int countFormatSpecifiers(String t) {
        int count = 0, i = 0;
        while ((i = t.indexOf("%d", i)) != -1) {
            count++;
            i += 2;
        }
        return count;
    }

    private static int etaFallback(Priority p) {
        return ETAPredictor.predictETA(p, 0, false);
    }
}
