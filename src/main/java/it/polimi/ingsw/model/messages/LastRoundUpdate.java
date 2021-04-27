package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

/**
 * Update sent when the last round begins, after a player has ended the game
 * by finishing the faith track or buying 7 cards.
 */
public class LastRoundUpdate extends PlayerPropertyUpdate {
    /**
     * Constructor: creates a new FaithUpdate.
     * @param playerName the player that ended the game
     **/
    public LastRoundUpdate(String playerName) {
        super(playerName);
    }

    @Override
    public void process(View view) {
        //TODO implement
    }
}
