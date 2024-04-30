package nassermohamedit.wordlesolver;

import java.util.*;

public class Wordle {

    public static final int CORRECT = 1;
    public static final int PRESENT = 0;
    public static final int ABSENT = -1;

    private final Config config;
    private final String word;
    private final int[] charCount = new int[26];
    private int triesLeft;
    private boolean won;

    public Wordle(Config config, String word) {
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

    public int[] guess(String guessed) {
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
        int[] feedback = new int[config.length];
        for (int i = 0; i < config.length; ++i) {
            if (charCount[guessed.charAt(i)] == 0) feedback[i] = ABSENT;
            else if (guessed.charAt(i) == word.charAt(i)) feedback[i] = CORRECT;
            else feedback[i] = PRESENT;
        }
        checkResult(feedback);
        return feedback;
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

    public static class Config {
        public final int length;
        public final int numberOfTries;
        private final Set<String> validGuesses;

        public Config(int length, Set<String> validGuesses, int numberOfTries) {
            this.length = length;
            this.numberOfTries = numberOfTries;
            this.validGuesses = new HashSet<>(validGuesses);
        }

        public Set<String> validGuesses() {
            return Collections.unmodifiableSet(validGuesses);
        }
    }
}
