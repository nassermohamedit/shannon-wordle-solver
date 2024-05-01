package nassermohamedit.wordlesolver;

import java.util.*;

public class Wordle {

    public static int nextId = 0;

    public static final int CORRECT = 1;

    public static final int PRESENT = 0;

    public static final int ABSENT = -1;

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
        triesLeft = config.numberOfTries;
        for (int i = 0; i < config.length; ++i) {
            ++charCount[word.charAt(i) - 'a'];
        }
    }

    public GuessResult guess(String guessed) {
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
        int[] result = new int[config.length];
        for (int i = 0; i < config.length; ++i) {
            if (charCount[guessed.charAt(i) - 'a'] == 0) {
                result[i] = ABSENT;
            }
            else if (guessed.charAt(i) == word.charAt(i)) {
                result[i] = CORRECT;
            }
            else {
                result[i] = PRESENT;
            }
        }
        for (int i = 0; i < config.length; ++i) {
            if (result[i] == PRESENT) {
                int count = charCount[guessed.charAt(i) - 'a'];
                for (int j = 0; j < config.length; ++j) {
                    if (result[j] == CORRECT && guessed.charAt(j) == guessed.charAt(i)) {
                        --count;
                    }
                }
                for (int j = 0; j < i && count > 0; ++j) {
                    if (result[j] == PRESENT && guessed.charAt(j) == guessed.charAt(i)) {
                        --count;
                    }
                }
                if (count <= 0) {
                    result[i] = ABSENT;
                }
            }
        }
        checkResult(result);
        return new GuessResult(guessed, result, uid);
    }

    private void checkResult(int[] feedback) {
        if (Arrays.stream(feedback).allMatch(x -> x == CORRECT))
            won = true;
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

    public static class GuessResult {

        private final int wordleId;

        private final String word;

        private final List<Integer> result;

        private GuessResult(String word, int[] result, int wordleId) {
            this.wordleId = wordleId;
            this.word = word;
            this.result = Arrays.stream(result).boxed().toList();
        }

        public String word() {
            return word;
        }

        public List<Integer> result() {
            return result;
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
            if (!(obj instanceof GuessResult other)) {
                return false;
            }
            if (other.wordleId != wordleId) {
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
