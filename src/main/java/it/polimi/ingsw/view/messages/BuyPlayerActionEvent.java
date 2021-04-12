package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;

import java.io.Serial;
import java.util.UUID;

public class BuyPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = 5030461192380878280L;

    private UUID developmentCardUuid;
    private int slot;

    public BuyPlayerActionEvent(String playerName, UUID developmentCardUuid, int slot) {
        super(playerName);
        this.developmentCardUuid = developmentCardUuid;
        this.slot = slot;
    }

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().buyDevelopmentCard(getPlayer(controller),
                controller.getGame().getDeck().getDevelopmentCardByUuid(developmentCardUuid), slot);
    }
}
