package nassermohamedit.wordlesolver;

import java.util.Optional;
import java.util.Set;

import static nassermohamedit.wordlesolver.Wordle.*;

public class RandomGuesser implements Guesser {

    private final int wordleId;

    private String cached = null;

    private Set<GuessResult> information;

    private final Config config;

    public RandomGuesser(Config config, int wordleId) {
        this.wordleId = wordleId;
        this.config = config;
    }

    @Override
    public String guess(GuessResult newInfo) {
        if (newInfo == null) {
            if (cached == null) {
                cached = doGuess();
            }
            return cached;
        }
        if (newInfo.wordleId() != wordleId) {
            throw new IllegalArgumentException();
        }
        if (information.contains(newInfo)) {
            return cached;
        }
        information.add(newInfo);
        cached = doGuess();
        return cached;
    }

    private String doGuess() {
        return "";
    }
}
