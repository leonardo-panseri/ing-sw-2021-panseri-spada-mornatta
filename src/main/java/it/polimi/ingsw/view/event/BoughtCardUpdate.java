package it.polimi.ingsw.view.event;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.ingsw.constant.GsonParser;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.player.Player;

public class BoughtCardUpdate extends PropertyUpdate{
    Player player;
    DevelopmentCard card;

    @Override
    public String serialize() {
        JsonObject content = new JsonObject();
        content.add("playerName", new JsonPrimitive(player.getNick()));
        content.add("card", GsonParser.instance().getGson().toJsonTree(card));
        return getSerialization(content.getAsString());
    }
}
