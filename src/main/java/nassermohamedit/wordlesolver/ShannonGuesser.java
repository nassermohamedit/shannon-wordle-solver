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

    //private final int[][][] similarity;

    private final Set<Integer> choices = new HashSet<>();

    private final char[] pattern;

    private final int[][] minimax = new int[26][2];;

    private final int[] count = new int[26];

    private final int[] infoArray;

    public ShannonGuesser(Config config, int wordleId) {
        this.wordleId = wordleId;
        this.length = config.length;
        this.size = config.validGuesses().size();
        this.dictionary = config.validGuesses().toArray(new String[size]);
        //this.similarity = new int[size][size][length];
        this.pattern = new char[length];
        int sizeOfOmega = (int) Math.pow(3, length);
        this.infoArray = new int[sizeOfOmega];
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
        if (newInfo != null) {
            updateInformation();
            choices.removeIf(i -> shouldExclude(dictionary[i]));
        }
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

    private boolean shouldExclude(String word) {
        for (int i = 0; i < length; ++i) {
            if (pattern[i] != '*' && pattern[i] != word.charAt(i)) {
                return true;
            }
        }
        Arrays.fill(count, 0);
        for (int i = 0; i < length; ++i) {
            ++count[word.charAt(i) - 'a'];
        }
        for (int i = 0; i < length; ++i) {
            int k = word.charAt(i) - 'a';
            if (count[k] < minimax[k][0] || count[k] > minimax[k][1]) {
                return true;
            }
        }
        return false;
    }

    private String findWordOfMaxEntropy() {
        double maxEntropy = Integer.MIN_VALUE;
        String maxWord = dictionary[0];
        for (int i = 0; i < size; ++i) {
            Arrays.fill(infoArray, 0);
            double entropy = 0;
            for (int j: choices) {
                int idx = indexOf(Wordle.resultOf(dictionary[j], dictionary[i]));
                ++infoArray[idx];
            }
            for (int c: infoArray) {
                double p = (double) c / (double) choices.size();
                entropy += p * informationOf(p);
            }
            if (entropy > maxEntropy) {
                maxEntropy = entropy;
                maxWord = dictionary[i];
            }
        }
        return maxWord;
    }

    private static double informationOf(double p) {
        if (p <= 0) return 0;
        return -Math.log(p) / Math.log(2);
    }

    public static int indexOf(int[] arr) {
        int idx = 0;
        for (int i = 0; i < arr.length; ++i) {
            idx += arr[i] * (int) Math.pow(3, i);
        }
        return idx;
    }

    private void initialize() {
        Arrays.fill(pattern, '*');
        for (int i = 0; i < 26; ++i) {
            minimax[i][0] = 0;
            minimax[i][1] = length;
        }
        for (int i = 0; i < size; ++i) {
            choices.add(i);
            //for (int j = 0; j < size; ++j) {
                //similarity[i][j] = Wordle.resultOf(dictionary[i], dictionary[j]);
            //}
        }
    }
}
