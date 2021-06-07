package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.Map;

/**
 * Update sent upon modification of the player's leader cards
 * (after initial draw, activation or discard of one of them)
 */
public class OwnedLeadersUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = -1261070615297828486L;

    private final Map<LeaderCard, Boolean> leaderCards;

    /**
     * Constructor: creates a new OwnedLeadersUpdate.
     *
     * @param playerName  the player affected by the modification
     * @param leaderCards the new map of leaders, with activation values
     **/
    public OwnedLeadersUpdate(String playerName, Map<LeaderCard, Boolean> leaderCards) {
        super(playerName);
        this.leaderCards = leaderCards;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateLeaderCards(getPlayerName(), leaderCards);
    }
}
