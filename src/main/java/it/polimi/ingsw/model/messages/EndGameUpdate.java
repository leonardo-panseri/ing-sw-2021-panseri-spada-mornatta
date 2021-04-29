package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.util.Map;

/**
 * Update sent after the game has ended.
 */
public class EndGameUpdate extends PropertyUpdate {
    private final Map<String, Integer> scores;
    private final String winnerName;

    /**
     * Constructor: creates a new EndGameUpdate.
     * @param scores a map containing player names with their relative scores
     * @param winnerName the nick of the winner
     * */
    public EndGameUpdate(Map<String, Integer> scores, String winnerName) {
        this.scores = scores;
        this.winnerName = winnerName;
    }

    @Override
    public void process(View view) {
        view.handleEndGame(scores, winnerName);
    }
}
