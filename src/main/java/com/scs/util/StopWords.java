package com.scs.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class StopWords {

    private static final String[] ENGLISH = {
            "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any",
            "are", "as", "at", "be", "because", "been", "before", "being", "below", "between",
            "both", "but", "by", "can", "cannot", "could", "did", "do", "does", "doing", "down",
            "during", "each", "few", "for", "from", "further", "had", "has", "have", "having",
            "he", "her", "here", "hers", "herself", "him", "himself", "his", "how", "i", "if",
            "in", "into", "is", "it", "its", "itself", "just", "like", "many", "me", "might",
            "more", "most", "much", "must", "my", "myself", "near", "no", "nor", "not", "now",
            "of", "off", "on", "once", "one", "only", "or", "other", "others", "ought", "our",
            "ours", "ourselves", "out", "over", "own", "please", "same", "shall", "she",
            "should", "so", "some", "such", "than", "that", "the", "their", "theirs", "them",
            "themselves", "then", "there", "these", "they", "this", "those", "through", "to",
            "too", "under", "until", "up", "upon", "us", "very", "was", "we", "were", "what",
            "when", "where", "which", "while", "who", "whom", "why", "will", "with", "within",
            "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves",
            "get", "got", "getting", "let", "lets", "letting", "make", "makes", "making",
            "made", "go", "goes", "going", "gone", "went", "come", "comes", "coming", "came",
            "put", "puts", "putting", "take", "takes", "taking", "took", "taken", "say", "says",
            "said", "saying", "see", "sees", "seeing", "saw", "seen", "know", "knows", "knew",
            "known", "think", "thinks", "thought", "look", "looks", "looking", "looked", "want",
            "wants", "wanted", "wanting", "give", "gives", "giving", "gave", "given", "tell",
            "tells", "telling", "told", "also", "any", "around", "upon", "being", "been", "was",
            "thank", "thanks", "hi", "hello", "hey", "ok", "okay", "yes", "yeah", "no",
            "today", "yesterday", "tomorrow", "now", "later", "then"
    };

    public static final Set<String> SET;
    static {
        Set<String> s = new HashSet<>(ENGLISH.length * 2);
        for (String w : ENGLISH) s.add(w);
        SET = Collections.unmodifiableSet(s);
    }

    private StopWords() {}

    public static boolean isStopWord(String word) {
        return word != null && SET.contains(word.toLowerCase());
    }
}
