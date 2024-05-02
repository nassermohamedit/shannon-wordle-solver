package nassermohamedit.wordlesolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static nassermohamedit.wordlesolver.Wordle.*;

class WordleTest {

    private static Wordle.Config config;

    private Wordle wordle;

    @BeforeAll
    static void initializeWordleConfig() {
        Set<String> validGuesses = new HashSet<>();
        String filePath = "wordle-data/allowed_words.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.lines().forEach(validGuesses::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config = new Wordle.Config(5, 6, validGuesses);
    }


    @Test
    void notAValidGuess_ShouldThrowIllegalArgumentException() {
        Wordle wordle = new Wordle(config, "honey");
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> wordle.guess("zzzzz")
        );
    }

    @Test
    void notOfValidLength_ShouldThrowIllegalArgumentException() {
        Wordle wordle = new Wordle(config, "honey");
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> wordle.guess("strawberry")
        );
    }

    @Test
    void allAbsent_ShouldReturnArrayOfAbsents() {
        Wordle wordle = new Wordle(config, "honey");
        int[] actual = wordle.guess("judas").result();
        int[] expected = new int[] {ABSENT, ABSENT, ABSENT, ABSENT, ABSENT};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void allPresent_ShouldReturnArrayOfPresents() {
        Wordle wordle = new Wordle(config, "honey");
        int[] actual = wordle.guess("yehno").result();
        int[] expected = new int[] {PRESENT, PRESENT, PRESENT, PRESENT, PRESENT};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void allCorrect_ShouldReturnArrayOfCorrects() {
        Wordle wordle = new Wordle(config, "honey");
        int[] actual = wordle.guess("honey").result();
        int[] expected = new int[] {CORRECT, CORRECT, CORRECT, CORRECT, CORRECT};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void randomGuessTest1() {
        Wordle wordle = new Wordle(config, "sweet");
        int[] actual = wordle.guess("tweet").result();
        int[] expected = new int[] {ABSENT, CORRECT, CORRECT, CORRECT, CORRECT};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void randomGuessTest2() {
        Wordle wordle = new Wordle(config, "alien");
        int[] actual = wordle.guess("annoy").result();
        int[] expected = new int[] {CORRECT, PRESENT, ABSENT, ABSENT, ABSENT};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void randomGuessTest4() {
        Wordle wordle = new Wordle(config, "nerdy");
        int[] actual = wordle.guess("nanny").result();
        int[] expected = new int[] {CORRECT, ABSENT, ABSENT, ABSENT, CORRECT};
        Assertions.assertArrayEquals(expected, actual);
    }
}
