package it.polimi.ingsw.model.lorenzo.action;

import it.polimi.ingsw.controller.LorenzoController;
import it.polimi.ingsw.model.card.CardColor;

public class DevelopmentAction extends LorenzoAction {
    private final CardColor cardColor;

    public DevelopmentAction(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    @Override
    public void execute(LorenzoController controller) {
        controller.executeDevelopmentAction(cardColor);
    }

    @Override
    public String toString() {
        return "Lorenzo discarded 2 " + cardColor + " development cards!";
    }
}
