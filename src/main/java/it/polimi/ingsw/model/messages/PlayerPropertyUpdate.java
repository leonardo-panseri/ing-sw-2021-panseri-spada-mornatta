package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * Update regarding modifications of one player's properties.
 */
public abstract class PlayerPropertyUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -8078905142506247738L;

    private final String playerName;

    /**
     * Constructor: creates a new PlayerPropertyUpdate.
     * @param playerName the player affected by the change
     **/
    public PlayerPropertyUpdate(String playerName) {
        this.playerName = playerName;
    }

    protected boolean isCurrentPlayer(View view) {
        return view.getPlayerName().equals(playerName);
    }

    protected String getPlayerName() {
        return this.playerName;
    }
}
