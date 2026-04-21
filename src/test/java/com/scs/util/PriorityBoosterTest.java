package com.scs.util;

import com.scs.model.Complaint.Priority;
import org.junit.Test;

import static org.junit.Assert.*;

public class PriorityBoosterTest {

    @Test
    public void fourthComplaintBoostsLowToMedium() {
        // Section 12: "User's 4th complaint this month → Priority boosted by one level"
        assertEquals(Priority.MEDIUM, PriorityBooster.boost(Priority.LOW, 4, 0));
    }

    @Test
    public void fifthComplaintBoostsMediumToHigh() {
        assertEquals(Priority.HIGH, PriorityBooster.boost(Priority.MEDIUM, 5, 0));
    }

    @Test
    public void belowThresholdNoBoostFromRepeat() {
        assertEquals(Priority.LOW,    PriorityBooster.boost(Priority.LOW,    2, 0));
        assertEquals(Priority.MEDIUM, PriorityBooster.boost(Priority.MEDIUM, 4, 0));
    }

    @Test
    public void staleUnresolvedBumpsOneLevel() {
        assertEquals(Priority.MEDIUM, PriorityBooster.boost(Priority.LOW,    1, 1));
        assertEquals(Priority.HIGH,   PriorityBooster.boost(Priority.MEDIUM, 1, 1));
        assertEquals(Priority.CRITICAL, PriorityBooster.boost(Priority.HIGH, 1, 1));
    }

    @Test
    public void criticalDoesNotExceedCritical() {
        assertEquals(Priority.CRITICAL, PriorityBooster.boost(Priority.CRITICAL, 10, 3));
    }

    @Test
    public void repeatAndStaleStackCleanly() {
        // 4 recent + LOW → MEDIUM, then stale → HIGH
        assertEquals(Priority.HIGH, PriorityBooster.boost(Priority.LOW, 4, 1));
    }

    @Test
    public void nullPriorityTreatedAsMedium() {
        assertEquals(Priority.MEDIUM, PriorityBooster.boost(null, 0, 0));
    }
}
