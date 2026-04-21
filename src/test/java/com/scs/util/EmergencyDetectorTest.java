package com.scs.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmergencyDetectorTest {

    @Test
    public void fireInBuildingHelp() {
        EmergencyDetector.Result r = EmergencyDetector.detect("fire in building help", "");
        assertTrue(r.isEmergency);
        assertTrue(r.matched.contains("fire"));
    }

    @Test
    public void accidentIsEmergency() {
        EmergencyDetector.Result r = EmergencyDetector.detect(
                "Water leakage terrible accident near hostel", "");
        assertTrue(r.isEmergency);
        assertTrue(r.matched.contains("accident"));
    }

    @Test
    public void noEmergency() {
        EmergencyDetector.Result r = EmergencyDetector.detect(
                "Please fix streetlight near park", "");
        assertFalse(r.isEmergency);
        assertTrue(r.matched.isEmpty());
    }

    @Test
    public void multiWordKeyword() {
        EmergencyDetector.Result r = EmergencyDetector.detect(
                "Suspected gas leak in the basement", "");
        assertTrue(r.isEmergency);
        assertTrue(r.matched.contains("gas leak"));
    }

    @Test
    public void wordBoundaryPreventsFalsePositive() {
        // "firestone" should not match the word "fire"
        EmergencyDetector.Result r = EmergencyDetector.detect(
                "Firestone retailer sign is broken", "");
        assertFalse("'firestone' should not trigger 'fire' match", r.isEmergency);
    }

    @Test
    public void detectsInDescription() {
        EmergencyDetector.Result r = EmergencyDetector.detect(
                "Building issue",
                "The old wall shows signs of imminent collapse");
        assertTrue(r.isEmergency);
        assertTrue(r.matched.contains("collapse"));
    }

    @Test
    public void nullSafe() {
        EmergencyDetector.Result r = EmergencyDetector.detect(null, null);
        assertFalse(r.isEmergency);
    }
}
