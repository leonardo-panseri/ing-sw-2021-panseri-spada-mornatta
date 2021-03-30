package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.player.Player;

public class TurnUpdate extends PropertyUpdate {
    private Player currentPlayer;

    public TurnUpdate(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String serialize() {
        return null;
    }
}
