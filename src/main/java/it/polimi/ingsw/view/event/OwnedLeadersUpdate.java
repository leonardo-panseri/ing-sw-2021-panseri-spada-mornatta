package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.Map;

public class OwnedLeadersUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -1261070615297828486L;

    private final String playerName;
    private final Map<LeaderCard, Boolean> leaderCards;

    public OwnedLeadersUpdate(String playerName, Map<LeaderCard, Boolean> leaderCards) {
        this.playerName = playerName;
        this.leaderCards = leaderCards;
    }

    @Override
    public void process(View view) {

    }
}
