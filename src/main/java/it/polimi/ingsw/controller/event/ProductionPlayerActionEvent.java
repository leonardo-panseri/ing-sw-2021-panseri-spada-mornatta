package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;

import java.util.List;
import java.util.UUID;

public class ProductionPlayerActionEvent extends PlayerActionEvent {
    private UUID cardUUID;
    private Resource[] baseProductionInput;
    private Resource baseProductionOutput;

    @Override
    public void process(GameController controller) {
        if(cardUUID == null) {
            controller.getPlayerController().useBaseProduction(getPlayer(controller), baseProductionInput, baseProductionOutput);
        } else {
            Card card = controller.getGame().getDeck().getLeaderCardByUuid(cardUUID);
            if(card == null) controller.getGame().getDeck().getDevelopmentCardByUuid(cardUUID);
            controller.getPlayerController().useProduction(getPlayer(controller), card);
        }
    }
}
