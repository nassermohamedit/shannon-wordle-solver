package nassermohamedit.wordlesolver;


public class Solver {

    protected final Wordle wordle;

    private final Guesser guesser;

    public Solver(Wordle wordle, Guesser guesser) {
        this.wordle = wordle;
        this.guesser = guesser;
    }
    
    public final boolean solve() {
        Wordle.GuessSimilarity result = null;
        String guessed;
        while (wordle.triesLeft() > 0 && !wordle.isWon()) {
            guessed = guesser.guess(result);
            result = wordle.guess(guessed);
            System.out.println(guessed);

        }
        return wordle.isWon();
    }
}
