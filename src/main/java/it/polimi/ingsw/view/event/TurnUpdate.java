package it.polimi.ingsw.view.event;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.ingsw.model.player.Player;

public class TurnUpdate extends PropertyUpdate {
    private final String currentPlayerName;

    public TurnUpdate(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }
}
