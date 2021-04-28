package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

/**
 * Update sent when a single player game ends.
 */
public class EndSingleplayerGameUpdate extends PropertyUpdate {
    private final boolean lorenzoWin;
    private final String loseReason;
    private final int playerScore;

    /**
     * Constructs a new EndSinglePlayerGameUpdate with the given circumstances.
     *
     * @param lorenzoWin boolean representing if Lorenzo has won
     * @param loseReason the reason why Lorenzo has won, if lorenzoWin is false, this will be ignored
     * @param playerScore the score of the Player, if lorenzoWin is true, this will be ignored
     */
    public EndSingleplayerGameUpdate(boolean lorenzoWin, String loseReason, int playerScore) {
        this.lorenzoWin = lorenzoWin;
        this.loseReason = loseReason;
        this.playerScore = playerScore;
    }

    @Override
    public void process(View controller) {
        //TODO Implement
    }
}
