package nassermohamedit.wordlesolver;

import java.util.*;

public class Solver {

    protected final Wordle wordle;

    private final Guesser guesser;

    public Solver(Wordle wordle, Guesser guesser) {
        this.wordle = wordle;
        this.guesser = guesser;
    }
    
    public final boolean solve() {
        Wordle.GuessResult result = null;
        String guessed;
        while (wordle.triesLeft() > 0) {
            guessed = guesser.guess(result);
            result = wordle.guess(guessed);
        }
        return wordle.isWon();
    }
}
