package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * Update sent upon modification of a player's faith.
 */
public class FaithUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 2004064498686999989L;

    private final int faithPoints;

    /**
     * Constructor: creates a new FaithUpdate.
     *
     * @param playerName  the player affected by the change
     * @param faithPoints the new faith points of the player
     */
    public FaithUpdate(String playerName, int faithPoints) {
        super(playerName);
        this.faithPoints = faithPoints;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateFaith(getPlayerName(), faithPoints);
    }
}
