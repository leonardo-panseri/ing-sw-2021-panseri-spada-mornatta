package it.polimi.ingsw.view.messages.production;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public abstract class Production implements Serializable {
    @Serial
    private static final long serialVersionUID = -4444977248750433737L;

    public abstract Map<Resource, Integer> use(GameController controller, Player player) throws IllegalArgumentException;
}
