package nassermohamedit.wordlesolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<String> validGuesses = new HashSet<>();
        String filePath = "wordle-data/allowed_words.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.lines().filter(l -> l.length() == 5).forEach(validGuesses::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Wordle.Config config = new Wordle.Config(5, 10, validGuesses);
        Wordle wordle = new Wordle(config, "bison");
        Guesser guesser = new ShannonGuesser(config, wordle.uid());
        Solver solver = new Solver(wordle, guesser);
        solver.solve();
    }
}
