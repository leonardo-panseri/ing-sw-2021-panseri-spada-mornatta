package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * Update sent when the last round begins, after a player has ended the game
 * by finishing the faith track or buying 7 cards.
 */
public class LastRoundUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = -3370016451218144806L;

    private final boolean hasLastPlayerCompleted;

    /**
     * Constructor: creates a new FaithUpdate.
     *
     * @param playerName             the player that ended the game
     * @param hasLastPlayerCompleted indicates if the player that has completed the game is the last player
     **/
    public LastRoundUpdate(String playerName, boolean hasLastPlayerCompleted) {
        super(playerName);
        this.hasLastPlayerCompleted = hasLastPlayerCompleted;
    }

    @Override
    public void process(View view) {
        if (!hasLastPlayerCompleted) {
            view.getRenderer().showLobbyMessage("Player " + getPlayerName() +
                    " has completed the game! This is the last round!");
        }
    }
}
