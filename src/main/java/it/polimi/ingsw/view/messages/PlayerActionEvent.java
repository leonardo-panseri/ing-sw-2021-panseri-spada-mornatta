package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.IProcessablePacket;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.io.Serializable;

public abstract class PlayerActionEvent implements Serializable, IProcessablePacket<GameController> {
    @Serial
    private static final long serialVersionUID = 2332021003720296652L;

    private transient Player player;

    protected Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}


