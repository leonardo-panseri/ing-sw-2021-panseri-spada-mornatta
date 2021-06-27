package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.Map;

/**
 * Updates sent for the initial turn.
 */
public class InitialTurnUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 8483292607940945299L;

    private final Map<LeaderCard, Boolean> leaderCards;
    private final int resourceToChoose;

    /**
     * Constructs a new InitialTurnUpdate.
     *
     * @param playerName the player name
     * @param leaderCards the initial draw of leader cards for this player
     * @param resourceToChoose the number of resources that this player must pick
     */
    public InitialTurnUpdate(String playerName, Map<LeaderCard, Boolean> leaderCards, int resourceToChoose) {
        super(playerName);
        this.leaderCards = leaderCards;
        this.resourceToChoose = resourceToChoose;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().handleInitialTurn(getPlayerName(), leaderCards, resourceToChoose);
    }
}
