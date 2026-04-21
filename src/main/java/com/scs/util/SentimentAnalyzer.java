package com.scs.util;

import com.scs.model.Complaint.Priority;

import java.util.HashMap;
import java.util.Map;

public final class SentimentAnalyzer {

    public static final String ANGRY    = "ANGRY";
    public static final String NEGATIVE = "NEGATIVE";
    public static final String NEUTRAL  = "NEUTRAL";
    public static final String POSITIVE = "POSITIVE";

    private static final Map<String, Integer> NEGATIVE_WORDS = new HashMap<>();
    private static final Map<String, Integer> POSITIVE_WORDS = new HashMap<>();
    private static final Map<String, Integer> URGENT_WORDS   = new HashMap<>();

    static {
        int[] neg2 = {-2};
        int[] neg1 = {-1};
        int[] neg3 = {-3};

        String[] n2 = {"terrible","worst","disgusting","horrible","pathetic","awful","worse",
                       "unacceptable","ridiculous","hate","disgust","rotten","filthy","garbage",
                       "mess","nightmare","outrageous","shameful","appalling"};
        String[] n1 = {"angry","frustrated","useless","poor","broken","bad","annoying","dirty",
                       "slow","disappointed","failed","fail","late","missed","stuck","lost",
                       "damage","damaged","faulty","defective","unreliable","unhappy","upset",
                       "complaint","problem","issue","not","never","no","nothing","nobody"};
        for (String w : n2) NEGATIVE_WORDS.put(w, -2);
        for (String w : n1) NEGATIVE_WORDS.put(w, -1);

        String[] p2 = {"great","appreciate","excellent","wonderful","awesome","grateful","love",
                       "fantastic","amazing","perfect","brilliant"};
        String[] p1 = {"good","thanks","thank","please","kindly","nice","happy","pleased",
                       "okay","fine","helpful","useful"};
        for (String w : p2) POSITIVE_WORDS.put(w, 2);
        for (String w : p1) POSITIVE_WORDS.put(w, 1);

        String[] u2 = {"urgent","immediately","asap","dangerous","risk","unsafe","hazard",
                       "injured","injury","critical","serious","severe","immediate"};
        String[] u3 = {"emergency","accident","collapse","fatal","lethal","danger","catastrophic",
                       "life-threatening"};
        for (String w : u2) URGENT_WORDS.put(w, -2);
        for (String w : u3) URGENT_WORDS.put(w, -3);
    }

    private SentimentAnalyzer() {}

    public static Result analyze(String text) {
        if (text == null) return new Result(NEUTRAL, Priority.MEDIUM, 0);
        int score = 0;
        String[] tokens = text.toLowerCase().split("[^a-z0-9]+");
        for (String t : tokens) {
            if (t.isEmpty()) continue;
            Integer v = NEGATIVE_WORDS.get(t);
            if (v == null) v = POSITIVE_WORDS.get(t);
            if (v == null) v = URGENT_WORDS.get(t);
            if (v != null) score += v;
        }

        String sentiment;
        Priority priority;
        if (score <= -5) {
            sentiment = ANGRY;    priority = Priority.HIGH;
        } else if (score <= -2) {
            sentiment = NEGATIVE; priority = Priority.MEDIUM;
        } else if (score >= 1) {
            sentiment = POSITIVE; priority = Priority.LOW;
        } else {
            sentiment = NEUTRAL;  priority = Priority.MEDIUM;
        }
        return new Result(sentiment, priority, score);
    }

    public static final class Result {
        public final String sentiment;
        public final Priority priority;
        public final int score;
        public Result(String sentiment, Priority priority, int score) {
            this.sentiment = sentiment;
            this.priority = priority;
            this.score = score;
        }
        @Override public String toString() {
            return "SentimentResult{" + sentiment + ", priority=" + priority + ", score=" + score + "}";
        }
    }
}
