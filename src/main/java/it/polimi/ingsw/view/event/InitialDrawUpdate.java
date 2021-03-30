package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class InitialDrawUpdate extends PropertyUpdate {
    private Player player;
    private List<LeaderCard> leaders;

    @Override
    public String serialize() {
        return null;
    }
}
