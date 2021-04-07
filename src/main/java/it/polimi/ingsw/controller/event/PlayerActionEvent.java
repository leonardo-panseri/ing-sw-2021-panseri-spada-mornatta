package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.constant.GsonParser;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.player.Player;

public abstract class PlayerActionEvent {
    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public abstract void process(GameController controller);

    public String serialize() {
        return "{\"type\":\"" + this.getClass().getSimpleName() + "\",\"content\":" + GsonParser.instance().getGson().toJson(this) + "}";
    }

    protected Player getPlayer(GameController controller) {
        return controller.getGame().getPlayerByName(playerName);
    }
}


