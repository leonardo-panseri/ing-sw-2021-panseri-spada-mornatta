package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.card.DevelopmentCard;

public class BuyPlayerActionEvent extends PlayerActionEvent {
    private DevelopmentCard developmentCard;
    private int slot;

    @Override
    public void process(GameController controller) {
        controller.buyDevelopmentCard(getPlayerName(), developmentCard, slot);
    }
}
