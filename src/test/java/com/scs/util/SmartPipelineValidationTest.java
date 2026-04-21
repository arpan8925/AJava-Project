package com.scs.util;

import com.scs.model.Complaint.Priority;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Integration tests for the validation table in PROJECT_PLAN.md Section 12.
 * Exercises multiple utilities against the exact inputs listed there.
 */
public class SmartPipelineValidationTest {

    // Row 1: "Water leakage terrible accident near hostel"
    //  → Sentiment=ANGRY, Priority=CRITICAL (emergency), Tags include water/leakage/hostel,
    //    Emergency=true
    @Test
    public void row1_waterLeakageTerribleAccident() {
        String title = "Water leakage terrible accident near hostel";
        String desc = "";

        SentimentAnalyzer.Result sentiment = SentimentAnalyzer.analyze(title + " " + desc);
        assertEquals("ANGRY", sentiment.sentiment);

        EmergencyDetector.Result emergency = EmergencyDetector.detect(title, desc);
        assertTrue(emergency.isEmergency);

        // Emergency override → CRITICAL
        Priority finalPriority = emergency.isEmergency ? Priority.CRITICAL : sentiment.priority;
        assertEquals(Priority.CRITICAL, finalPriority);

        List<String> tags = TagGenerator.generateTags(title, desc);
        assertTrue("tags should include 'water': "   + tags, tags.contains("water"));
        assertTrue("tags should include 'leakage': " + tags, tags.contains("leakage"));
        assertTrue("tags should include 'hostel': "  + tags, tags.contains("hostel"));
    }

    // Row 2: "Please fix streetlight near park"
    //  → Sentiment=POSITIVE, Priority=LOW, Tags include streetlight/park
    @Test
    public void row2_pleaseFixStreetlight() {
        String title = "Please fix streetlight near park";

        SentimentAnalyzer.Result sentiment = SentimentAnalyzer.analyze(title);
        assertEquals("POSITIVE", sentiment.sentiment);
        assertEquals(Priority.LOW, sentiment.priority);

        EmergencyDetector.Result emergency = EmergencyDetector.detect(title, "");
        assertFalse(emergency.isEmergency);

        List<String> tags = TagGenerator.generateTags(title, "");
        assertTrue(tags.contains("streetlight"));
        assertTrue(tags.contains("park"));
    }

    // Row 3: User's 4th complaint this month → priority boosted by one level
    @Test
    public void row3_fourthComplaintBoostsPriority() {
        Priority start = Priority.LOW;
        Priority boosted = PriorityBooster.boost(start, 4, 0);
        assertEquals(Priority.MEDIUM, boosted);

        Priority startMedium = Priority.MEDIUM;
        Priority boostedMedium = PriorityBooster.boost(startMedium, 5, 0);
        assertEquals(Priority.HIGH, boostedMedium);
    }

    // Row 4: "fire in building help"
    //  → Emergency detected, Priority=CRITICAL, admins to be notified
    @Test
    public void row4_fireInBuildingHelp() {
        String title = "fire in building help";
        EmergencyDetector.Result emergency = EmergencyDetector.detect(title, "");
        assertTrue(emergency.isEmergency);
        assertTrue(emergency.matched.contains("fire"));

        // Pipeline overrides priority to CRITICAL when emergency flagged
        Priority finalPriority = emergency.isEmergency ? Priority.CRITICAL : Priority.MEDIUM;
        assertEquals(Priority.CRITICAL, finalPriority);
    }
}
