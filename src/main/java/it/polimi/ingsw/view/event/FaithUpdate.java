package it.polimi.ingsw.view.event;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.ingsw.constant.GsonParser;
import it.polimi.ingsw.model.player.Player;

public class FaithUpdate extends PropertyUpdate {
    private String playerName;
    private int faithPoints;
    private int popeFavours;


    public FaithUpdate(String playerName, int faithPoints, int popeFavours) {
        this.playerName = playerName;
        this.faithPoints = faithPoints;
        this.popeFavours = popeFavours;
    }
}
