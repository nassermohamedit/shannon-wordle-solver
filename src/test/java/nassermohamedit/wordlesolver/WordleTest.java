package nassermohamedit.wordlesolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
        List<Integer> actual = wordle.guess("judas").result();
        List<Integer> expected = Arrays.asList(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void allPresent_ShouldReturnArrayOfPresents() {
        Wordle wordle = new Wordle(config, "honey");
        List<Integer> actual = wordle.guess("yehno").result();
        List<Integer> expected = Arrays.asList(PRESENT, PRESENT, PRESENT, PRESENT, PRESENT);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void allCorrect_ShouldReturnArrayOfCorrects() {
        Wordle wordle = new Wordle(config, "honey");
        List<Integer> actual = wordle.guess("honey").result();
        List<Integer> expected = Arrays.asList(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void randomGuessTest1() {
        Wordle wordle = new Wordle(config, "sweet");
        List<Integer> actual = wordle.guess("tweet").result();
        List<Integer> expected = Arrays.asList(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void randomGuessTest2() {
        Wordle wordle = new Wordle(config, "alien");
        List<Integer> actual = wordle.guess("annoy").result();
        List<Integer> expected = Arrays.asList(CORRECT, PRESENT, ABSENT, ABSENT, ABSENT);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void randomGuessTest4() {
        Wordle wordle = new Wordle(config, "nerdy");
        List<Integer> actual = wordle.guess("nanny").result();
        List<Integer> expected = Arrays.asList(CORRECT, ABSENT, ABSENT, ABSENT, CORRECT);
        Assertions.assertEquals(expected, actual);
    }
}
