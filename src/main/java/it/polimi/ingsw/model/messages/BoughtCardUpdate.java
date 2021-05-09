package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * Update sent after the player has successfully bought a development card.
 */
public class BoughtCardUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 5376131271721131000L;

    private final DevelopmentCard developmentCard;
    private final int slot;

    /**
     * Constructor: creates a new BoughtCardUpdate
     * @param playerName nick of the player that bought the card
     * @param developmentCard the bought card
     * @param slot the board slot where the card was stacked
     */
    public BoughtCardUpdate(String playerName, DevelopmentCard developmentCard, int slot) {
        super(playerName);
        this.developmentCard = developmentCard;
        this.slot = slot;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateDevelopmentCards(getPlayerName(), developmentCard, slot);
    }
}
