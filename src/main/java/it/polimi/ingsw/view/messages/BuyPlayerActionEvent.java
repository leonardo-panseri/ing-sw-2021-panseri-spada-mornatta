package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;

import java.io.Serial;
import java.util.UUID;

public class BuyPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = 5030461192380878280L;

    private UUID developmentCardUuid;
    private int slot;

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().buyDevelopmentCard(getPlayer(controller),
                controller.getGame().getDeck().getDevelopmentCardByUuid(developmentCardUuid), slot);
    }
}
