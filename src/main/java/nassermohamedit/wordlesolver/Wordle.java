package nassermohamedit.wordlesolver;

import java.util.*;

public class Wordle {

    public static int nextId = 0;

    public static final int CORRECT = 0;

    public static final int PRESENT = 1;

    public static final int ABSENT = 2;

    private final int uid;

    private final Config config;

    private final String word;

    private final int[] charCount = new int[26];

    private int triesLeft;

    private boolean won;


    public Wordle(Config config, String word) {
        this.uid = nextId++;
        if (config.length != word.length()) {
            throw new IllegalArgumentException();
        }
        this.config = config;
        this.word = word;
        this.triesLeft = config.numberOfTries;
        for (int i = 0; i < config.length; ++i) {
            ++charCount[word.charAt(i) - 'a'];
        }
    }

    public GuessSimilarity guess(String guessed) {
        if (triesLeft == 0 || won) {
            throw new IllegalStateException("The Game is finished");
        }
        if (guessed.length() != this.config.length) {
            throw new IllegalArgumentException("Guesses must be of length" + config.length);
        }
        if (!config.validGuesses.contains(guessed)) {
            throw new IllegalArgumentException("Not a valid guess");
        }
        --this.triesLeft;
        int similarity = similarityOf(word, guessed);
        if (similarity == 0) {
            won = true;
        }
        return new GuessSimilarity(uid, guessed, similarity);
    }

    public int triesLeft() {
        return this.triesLeft;
    }

    public boolean isWon() {
        return won;
    }

    public Config config() {
        return config;
    }

    public int uid() {
        return uid;
    }

    public static int similarityOf(String word, String other) {
        if (word.length() != other.length()) {
            throw new IllegalArgumentException("Words must be of the same length");
        }
        int[] count = new int[26];
        int length = word.length();
        int[] result = new int[length];
        for (int k = 0; k < length; ++k) {
            ++count[word.charAt(k) - 'a'];
        }
        for (int k = 0; k < length; ++k) {
            char c = other.charAt(k);
            int idx = c - 'a';
            if (c == word.charAt(k)) {
                result[k] = CORRECT;
                --count[idx];
            }
            else if (count[idx] == 0) {
                result[k] = ABSENT;
            }
            else {
                result[k] = PRESENT;
            }
        }
        for (int k = 0; k < length; ++k) {
            char c = other.charAt(k);
            int idx = c - 'a';
            if (result[k] == PRESENT) {
                if (count[idx] <= 0) {
                    result[k] = ABSENT;
                } else {
                    --count[idx];
                }
            }
        }
        return simArrToInt(length, result);
    }

    private static int simArrToInt(int length, int[] result) {
        int similarity = 0;
        for (int i = 0, p = 1; i < length; ++i, p *= 3) {
            similarity += result[i] * p;
        }
        return similarity;
    }


    public static class GuessSimilarity {

        private final int wordleId;

        private final String word;

        private final int similarity;

        private GuessSimilarity(int wordleId, String word, int similarity) {
            this.similarity = similarity;
            this.word = word;
            this.wordleId = wordleId;
        }

        public String word() {
            return word;
        }

        public int similarity() {
            return similarity;
        }

        public int wordleId() {
            return wordleId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof GuessSimilarity other)) {
                return false;
            }
            if (other.wordleId() != wordleId) {
                return false;
            }
            if (!other.word().equals(word)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89*hash + this.word.hashCode();
            hash = 89*hash + this.wordleId;
            return hash;
        }

    }

    public static final class Config {

        public final int length;

        public final int numberOfTries;

        private final Set<String> validGuesses;

        public Config(int length, int numberOfTries, Set<String> validGuesses) {
            this.length = length;
            this.numberOfTries = numberOfTries;
            this.validGuesses = new HashSet<>(validGuesses);
        }

        public Set<String> validGuesses() {
            return Collections.unmodifiableSet(validGuesses);
        }
    }
}
