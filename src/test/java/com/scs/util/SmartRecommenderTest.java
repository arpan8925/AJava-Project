package com.scs.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SmartRecommenderTest {

    @Test
    public void waterComplaintTriggersWaterSolutions() {
        List<String> out = SmartRecommender.recommend(
                "Pipe is leaking badly", "water is wasted", null);
        assertFalse(out.isEmpty());
        boolean hasWaterDept = out.stream().anyMatch(s -> s.toLowerCase().contains("water"));
        assertTrue("expected water-related solution: " + out, hasWaterDept);
    }

    @Test
    public void roadComplaintTriggersRoadSolutions() {
        List<String> out = SmartRecommender.recommend(
                "Huge pothole on the main road", "traffic jams every day", null);
        assertTrue(out.stream().anyMatch(s -> s.toLowerCase().contains("pothole")
                                           || s.toLowerCase().contains("road")));
    }

    @Test
    public void fallbackWhenNoMatch() {
        List<String> out = SmartRecommender.recommend("random title", "nothing to do", null);
        assertEquals(1, out.size());
    }

    @Test
    public void resultCappedAtFive() {
        List<String> out = SmartRecommender.recommend(
                "fire flood accident pothole wire garbage leak road drain tree",
                "", null);
        assertTrue("result should be <= 5 entries: " + out.size(), out.size() <= 5);
    }

    @Test
    public void explicitCategoryWins() {
        List<String> out = SmartRecommender.recommend("something", "else entirely", "electricity");
        assertFalse(out.isEmpty());
        boolean hasElectric = out.stream().anyMatch(s -> s.toLowerCase().contains("electricity")
                                                      || s.toLowerCase().contains("wire"));
        assertTrue(hasElectric);
    }
}
