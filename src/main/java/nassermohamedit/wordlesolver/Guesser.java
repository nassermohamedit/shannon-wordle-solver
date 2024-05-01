package nassermohamedit.wordlesolver;

import java.util.Optional;

public interface Guesser {

    String guess(Wordle.GuessResult information);
}
