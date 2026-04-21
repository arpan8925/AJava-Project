package com.scs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class SmartSummarizer {

    private static final int SHORT_TEXT_WORD_COUNT = 50;
    private static final int MIN_SCORED_WORD_LENGTH = 4;

    private SmartSummarizer() {}

    public static String summarize(String text, int maxSentences) {
        if (text == null) return "";
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return "";

        if (countWords(trimmed) < SHORT_TEXT_WORD_COUNT) {
            return trimmed;
        }

        String[] sentences = trimmed.split("(?<=[.!?])\\s+");
        if (sentences.length <= maxSentences) return trimmed;

        List<ScoredSentence> scored = new ArrayList<>(sentences.length);
        for (int i = 0; i < sentences.length; i++) {
            scored.add(new ScoredSentence(i, sentences[i], score(sentences[i])));
        }

        scored.sort(Comparator.comparingInt((ScoredSentence s) -> s.score).reversed());
        List<ScoredSentence> top = scored.subList(0, Math.min(maxSentences, scored.size()));

        List<ScoredSentence> inOrder = new ArrayList<>(top);
        inOrder.sort(Comparator.comparingInt(s -> s.index));

        StringBuilder out = new StringBuilder();
        for (ScoredSentence s : inOrder) {
            if (out.length() > 0) out.append(' ');
            out.append(s.sentence.trim());
        }
        return out.toString();
    }

    public static String summarize(String text) {
        return summarize(text, 2);
    }

    static int score(String sentence) {
        String[] tokens = sentence.toLowerCase().split("[^a-z0-9]+");
        int s = 0;
        for (String t : tokens) {
            if (t.length() >= MIN_SCORED_WORD_LENGTH && !StopWords.isStopWord(t)) s++;
        }
        return s;
    }

    private static int countWords(String text) {
        String[] parts = text.trim().split("\\s+");
        return parts.length == 1 && parts[0].isEmpty() ? 0 : parts.length;
    }

    static final class ScoredSentence {
        final int index;
        final String sentence;
        final int score;
        ScoredSentence(int index, String sentence, int score) {
            this.index = index;
            this.sentence = sentence;
            this.score = score;
        }
    }
}
