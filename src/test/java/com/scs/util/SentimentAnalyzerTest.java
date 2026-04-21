package com.scs.util;

import com.scs.model.Complaint.Priority;
import org.junit.Test;

import static org.junit.Assert.*;

public class SentimentAnalyzerTest {

    @Test
    public void angryText() {
        SentimentAnalyzer.Result r = SentimentAnalyzer.analyze(
                "Water leakage terrible accident near hostel");
        assertEquals("ANGRY", r.sentiment);
        assertEquals(Priority.HIGH, r.priority);
        assertTrue("score should be <= -5, was " + r.score, r.score <= -5);
    }

    @Test
    public void positiveText() {
        SentimentAnalyzer.Result r = SentimentAnalyzer.analyze("Please fix streetlight near park");
        assertEquals("POSITIVE", r.sentiment);
        assertEquals(Priority.LOW, r.priority);
    }

    @Test
    public void neutralText() {
        SentimentAnalyzer.Result r = SentimentAnalyzer.analyze("fire in building help");
        assertEquals("NEUTRAL", r.sentiment);
        assertEquals(Priority.MEDIUM, r.priority);
    }

    @Test
    public void negativeText() {
        SentimentAnalyzer.Result r = SentimentAnalyzer.analyze(
                "The streetlight is broken and I am frustrated");
        assertEquals("NEGATIVE", r.sentiment);
        assertEquals(Priority.MEDIUM, r.priority);
    }

    @Test
    public void nullAndEmpty() {
        assertEquals("NEUTRAL", SentimentAnalyzer.analyze(null).sentiment);
        assertEquals("NEUTRAL", SentimentAnalyzer.analyze("").sentiment);
    }
}
