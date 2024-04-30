package nassermohamedit.wordlesolver;

import java.util.*;

public abstract class AbstractSolver {

    private final Wordle wordle;

    private final Map<String, List<Integer>> feedbacks;

    public AbstractSolver(Wordle wordle) {
        this.wordle = wordle;
        feedbacks = new HashMap<>();
    }
    
    public final boolean solve() {
        while (wordle.triesLeft() > 0) {
            String guess = guess();
            int[] feedback = wordle.guess(guess);
            updateInformation(guess, feedback);

        }
        return wordle.isWon();
    }
    
    protected void updateInformation(String guess, int[] feedback) {
        feedbacks.put(guess, Arrays.stream(feedback).boxed().toList());
    }

    protected abstract String guess();
}
