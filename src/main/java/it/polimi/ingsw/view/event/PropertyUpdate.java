package it.polimi.ingsw.view.event;

import it.polimi.ingsw.constant.GsonParser;

public abstract class PropertyUpdate {

    /**
     * Serializes the object into a JSON string.
     *
     * @return a json string representing the object
     */
    public String serialize() {
        return "{\"type\":\"" + this.getClass().getSimpleName() + "\",\"content\":" + GsonParser.instance().getGson().toJson(this) + "}";
    }
}
