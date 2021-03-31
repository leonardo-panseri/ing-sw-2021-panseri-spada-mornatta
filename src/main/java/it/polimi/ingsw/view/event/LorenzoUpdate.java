package it.polimi.ingsw.view.event;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class LorenzoUpdate extends PropertyUpdate {
    private final int faithPoints;

    public LorenzoUpdate(int points) {
        faithPoints = points;
    }
}
