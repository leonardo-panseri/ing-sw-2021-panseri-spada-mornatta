package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.card.DevelopmentCard;

import java.util.UUID;

public class BuyPlayerActionEvent extends PlayerActionEvent {
    private UUID developmentCardUuid;
    private int slot;

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().buyDevelopmentCard(getPlayer(controller),
                controller.getGame().getDeck().getDevelopmentCardByUuid(developmentCardUuid), slot);
    }
}
