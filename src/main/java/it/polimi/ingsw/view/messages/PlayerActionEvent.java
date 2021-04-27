package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.io.Serializable;

public abstract class PlayerActionEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 2332021003720296652L;

    private transient Player player;

    public abstract void process(GameController controller);

    protected Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}


