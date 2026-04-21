package com.scs.util;

import com.scs.model.Complaint.Priority;
import org.junit.Test;

import static org.junit.Assert.*;

public class ETAPredictorTest {

    @Test
    public void basePriorities() {
        assertEquals(4,   ETAPredictor.predictETA(Priority.CRITICAL, 0, false));
        assertEquals(24,  ETAPredictor.predictETA(Priority.HIGH,     0, false));
        assertEquals(72,  ETAPredictor.predictETA(Priority.MEDIUM,   0, false));
        assertEquals(168, ETAPredictor.predictETA(Priority.LOW,      0, false));
    }

    @Test
    public void highLoadMultipliesBy1_5() {
        int base = 24;
        int withLoad = ETAPredictor.predictETA(Priority.HIGH, 21, false);
        assertEquals((int) Math.round(base * 1.5), withLoad);
    }

    @Test
    public void loadAtExactlyThresholdDoesNotMultiply() {
        int withoutLoad = ETAPredictor.predictETA(Priority.HIGH, 20, false);
        assertEquals(24, withoutLoad);
    }

    @Test
    public void repeatUserMultipliesBy0_8() {
        int base = 72;
        int withRepeat = ETAPredictor.predictETA(Priority.MEDIUM, 0, true);
        assertEquals((int) Math.round(base * 0.8), withRepeat);
    }

    @Test
    public void bothAdjustmentsCompound() {
        int base = 24;
        int combined = ETAPredictor.predictETA(Priority.HIGH, 50, true);
        assertEquals((int) Math.round(base * 1.5 * 0.8), combined);
    }

    @Test
    public void nullPriorityFallsBackToMedium() {
        assertEquals(72, ETAPredictor.predictETA(null, 0, false));
    }
}
