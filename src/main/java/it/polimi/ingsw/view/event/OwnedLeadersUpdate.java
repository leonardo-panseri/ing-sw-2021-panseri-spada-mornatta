package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.card.LeaderCard;

import java.util.Map;

public class OwnedLeadersUpdate extends PropertyUpdate {
    private final String playerName;
    private final Map<LeaderCard, Boolean> leaderCards;

    public OwnedLeadersUpdate(String playerName, Map<LeaderCard, Boolean> leaderCards) {
        this.playerName = playerName;
        this.leaderCards = leaderCards;
    }
}
