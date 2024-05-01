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
            br.lines().forEach(validGuesses::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Wordle.Config config = new Wordle.Config(5, 6, validGuesses);
        Wordle wordle = new Wordle(config, "apple");
        Guesser randomGuesser = new RandomGuesser(config, wordle.uid());
        Solver solver = new Solver(wordle, randomGuesser);
        solver.solve();
    }
}
