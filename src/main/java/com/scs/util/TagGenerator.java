package com.scs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TagGenerator {

    private static final int MIN_WORD_LENGTH = 4;
    private static final int DEFAULT_MAX_TAGS = 6;

    private TagGenerator() {}

    public static List<String> generateTags(String title, String description, int maxTags) {
        Map<String, Integer> freq = new HashMap<>();
        Set<String> orderSeen = new LinkedHashSet<>();

        accumulate(title,       freq, orderSeen, /*boostTitle=*/ 2);
        accumulate(description, freq, orderSeen, /*boost=*/       1);

        List<String> candidates = new ArrayList<>(orderSeen);
        candidates.sort((a, b) -> {
            int fa = freq.get(a), fb = freq.get(b);
            if (fa != fb) return Integer.compare(fb, fa);
            return Integer.compare(
                    new ArrayList<>(orderSeen).indexOf(a),
                    new ArrayList<>(orderSeen).indexOf(b));
        });

        return new ArrayList<>(candidates.subList(0, Math.min(maxTags, candidates.size())));
    }

    public static List<String> generateTags(String title, String description) {
        return generateTags(title, description, DEFAULT_MAX_TAGS);
    }

    public static String generateTagsString(String title, String description) {
        return String.join(",", generateTags(title, description));
    }

    private static void accumulate(String text, Map<String, Integer> freq,
                                   Set<String> orderSeen, int weight) {
        if (text == null || text.isEmpty()) return;
        String[] tokens = text.toLowerCase().split("[^a-z0-9]+");
        for (String t : tokens) {
            if (t.length() < MIN_WORD_LENGTH) continue;
            if (StopWords.isStopWord(t)) continue;
            orderSeen.add(t);
            freq.merge(t, weight, Integer::sum);
        }
    }
}
