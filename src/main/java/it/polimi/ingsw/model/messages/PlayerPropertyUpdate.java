package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public abstract class PlayerPropertyUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -8078905142506247738L;

    private final String playerName;

    public PlayerPropertyUpdate(String playerName) {
        this.playerName = playerName;
    }

    protected boolean isCurrentPlayer(View view) {
        return view.getPlayerName().equals(playerName);
    }
}
