package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;

import java.util.Map;

public class OwnedLeadersUpdate extends PropertyUpdate {
    private Player player;
    private Map<LeaderCard, Boolean> leaderCards;
}
