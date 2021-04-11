package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;

import java.io.Serial;
import java.util.UUID;

public class ActivateLeaderPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = 5957160381729381998L;

    private UUID leaderCardUUID;

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().activateLeaderCard(getPlayer(controller), controller.getGame().getDeck().getLeaderCardByUuid(leaderCardUUID));
    }
}
