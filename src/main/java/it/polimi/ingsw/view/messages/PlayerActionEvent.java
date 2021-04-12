package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.io.Serializable;

public abstract class PlayerActionEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 2332021003720296652L;

    private final String playerName;

    public PlayerActionEvent(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public abstract void process(GameController controller);

    protected Player getPlayer(GameController controller) {
        return controller.getGame().getPlayerByName(playerName);
    }
}


