package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;

import java.util.List;

public class ProductionPlayerActionEvent extends PlayerActionEvent {
    private Card card;
    private Resource[] baseProductionInput;
    private Resource baseProductionOutput;

    @Override
    public void process(GameController controller) {
        if(card == null) {
            controller.useBaseProduction(getPlayerName(), baseProductionInput, baseProductionOutput);
        } else {
            controller.useProduction(getPlayerName(), card);
        }
    }
}
