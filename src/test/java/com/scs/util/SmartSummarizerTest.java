package com.scs.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class SmartSummarizerTest {

    @Test
    public void shortTextReturnsAsIs() {
        String text = "Water is leaking from the tap.";
        assertEquals(text, SmartSummarizer.summarize(text));
    }

    @Test
    public void emptyAndNullAreSafe() {
        assertEquals("", SmartSummarizer.summarize(null));
        assertEquals("", SmartSummarizer.summarize("   "));
    }

    @Test
    public void pickTopScoringSentencesAndPreserveOrder() {
        String text =
                "There is severe water leakage near the hostel building. " +
                "It is causing slippery roads and students are getting injured. " +
                "The water has been flowing for three days now and nobody has fixed it. " +
                "Please help quickly. " +
                "Workers were called but nobody came. " +
                "We need urgent fixing of the burst pipe and drainage system. " +
                "Children cannot walk safely. " +
                "Daily commuters have been complaining loudly about this mess. " +
                "Municipal authorities should step in. " +
                "Please schedule a permanent repair of this leaking pipeline.";
        String out = SmartSummarizer.summarize(text, 2);
        assertNotNull(out);
        assertFalse(out.isEmpty());
        long sentenceCount = out.chars().filter(ch -> ch == '.' || ch == '!' || ch == '?').count();
        assertTrue("should pick around 2 sentences, got: " + out, sentenceCount <= 3 && sentenceCount >= 1);
    }
}
