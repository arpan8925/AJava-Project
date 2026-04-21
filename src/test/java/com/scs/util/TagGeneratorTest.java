package com.scs.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TagGeneratorTest {

    @Test
    public void waterLeakageHostel() {
        List<String> tags = TagGenerator.generateTags(
                "Water leakage terrible accident near hostel", "");
        assertTrue("expected 'water' in tags: "    + tags, tags.contains("water"));
        assertTrue("expected 'leakage' in tags: "  + tags, tags.contains("leakage"));
        assertTrue("expected 'hostel' in tags: "   + tags, tags.contains("hostel"));
    }

    @Test
    public void streetlightPark() {
        List<String> tags = TagGenerator.generateTags("Please fix streetlight near park", "");
        assertTrue("expected 'streetlight' in tags: " + tags, tags.contains("streetlight"));
        assertTrue("expected 'park' in tags: "        + tags, tags.contains("park"));
    }

    @Test
    public void stopWordsAndShortWordsExcluded() {
        List<String> tags = TagGenerator.generateTags("The fix is done in the park", "");
        assertFalse(tags.contains("the"));
        assertFalse(tags.contains("fix"));
        assertFalse(tags.contains("is"));
        assertFalse(tags.contains("in"));
    }

    @Test
    public void titleWordsWeightedHigherThanDescription() {
        List<String> tags = TagGenerator.generateTags(
                "broken pipe",
                "the street has some issues reported recently");
        assertEquals("broken", tags.get(0));
    }

    @Test
    public void emptyInputs() {
        assertTrue(TagGenerator.generateTags(null, null).isEmpty());
        assertTrue(TagGenerator.generateTags("", "").isEmpty());
    }

    @Test
    public void commaSeparatedString() {
        String s = TagGenerator.generateTagsString("water pipe burst", "");
        assertTrue(s.contains("water"));
        assertTrue(s.contains(","));
    }
}
