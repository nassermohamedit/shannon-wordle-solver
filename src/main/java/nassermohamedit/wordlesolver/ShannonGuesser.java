package nassermohamedit.wordlesolver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static nassermohamedit.wordlesolver.Wordle.*;

public class ShannonGuesser implements Guesser {

    private final int wordleId;

    private String cached = null;

    private GuessResult newInfo;

    private final int length;

    private final String[] dictionary;

    private final int size;

    private final int[][][] similarity;

    private final Set<Integer> choices = new HashSet<>();

    private final char[] pattern;

    private final int[][] minimax = new int[26][2];;

    private final int[] count = new int[26];

    private final int[] chars;

    public ShannonGuesser(Config config, int wordleId) {
        this.wordleId = wordleId;
        this.length = config.length;
        this.size = config.validGuesses().size();
        this.dictionary = config.validGuesses().toArray(new String[size]);
        this.similarity = new int[size][size][length];
        this.pattern = new char[length];
        this.chars = new int[length];
    }

    @Override
    public String guess(GuessResult newInfo) {
        if (newInfo == null && cached == null) {
            doGuess();
        }
        else if (newInfo != null) {
            if (newInfo.wordleId() != wordleId) {
                throw new IllegalArgumentException();
            }
            this.newInfo = newInfo;
            doGuess();
        }
        return cached;
    }

    private void doGuess() {
        if (cached == null) {
            initialize();
        }
        updateInformation();
        updateChoices();
        cached = findWordOfMaxEntropy();
    }

    private void updateInformation() {
        Arrays.fill(count, 0);
        String word = newInfo.word();
        int[] result = newInfo.result();
        for (int i = 0; i < length; ++i) {
            int k = word.charAt(i) - 'a';
            if (result[i] != ABSENT) {
                ++count[k];
                if (result[i] == CORRECT && pattern[i] == '*') {
                    pattern[i] = word.charAt(i);
                }
            }
        }
        for (int i = 0; i < length; ++i) {
            int k = word.charAt(i) - 'a';
            if (result[i] == ABSENT) {
                minimax[k][1] = count[k];
            } else {
                minimax[k][0] = Math.max(minimax[k][0], count[k]);
            }
        }
    }

    private void updateChoices() {
        // TODO
    }

    private String findWordOfMaxEntropy() {
        //TODO
        return "";
    }

    private void initialize() {
        Arrays.fill(pattern, '*');
        for (int i = 0; i < 26; ++i) {
            minimax[i][0] = 0;
            minimax[i][1] = length;
        }
        for (int i = 0; i < size; ++i) {
            choices.add(i);
            for (int j = 0; j < size; ++j) {
                similarity[i][j] = similarityOf(dictionary[i], dictionary[j]);
            }
        }
    }

    private int[] similarityOf(String w1, String w2) {
        // TODO
        return new int[] {0, 0, 0, 0, 0};
    }
}
