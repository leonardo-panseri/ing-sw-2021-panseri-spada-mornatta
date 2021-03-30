package it.polimi.ingsw.view.event;

public abstract class PropertyUpdate {

    /**
     * Serializes the object into a JSON string.
     *
     * @return a json string representing the object
     */
    public abstract String serialize();

    String getSerialization(String content) {
        return "{\"type\":\"" + this.getClass().getSimpleName() + "\",\"content\":" + content + "}";
    }
}
