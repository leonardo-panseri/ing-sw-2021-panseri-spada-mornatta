package it.polimi.ingsw.model.lorenzo.action;

import it.polimi.ingsw.controller.LorenzoController;
import it.polimi.ingsw.model.card.CardColor;

/**
 * LorenzoAction removing 2 development cards of the given color from the deck.
 */
public class DevelopmentAction extends LorenzoAction {
    private final CardColor cardColor;

    /**
     * Constructs a new DevelopmentAction for the given CardColor.
     *
     * @param cardColor the color of the cards that will be discarded by this action
     */
    public DevelopmentAction(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    @Override
    public boolean execute(LorenzoController controller) {
        return controller.executeDevelopmentAction(cardColor);
    }

    @Override
    public String toString() {
        return "Lorenzo discarded 2 " + cardColor.toString().toLowerCase() + " development cards!";
    }
}
