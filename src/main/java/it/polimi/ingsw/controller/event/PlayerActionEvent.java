package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.model.player.Player;

public abstract class PlayerActionEvent {
    private Player player;
    private boolean cancelled;

    public Player getPlayer() {
        return player;
    }
}


