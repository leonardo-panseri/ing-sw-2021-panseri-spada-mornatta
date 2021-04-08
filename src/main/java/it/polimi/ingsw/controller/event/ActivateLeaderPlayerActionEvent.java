package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;

import java.util.UUID;

public class ActivateLeaderPlayerActionEvent extends PlayerActionEvent {
    private UUID leaderCardUUID;

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().activateLeaderCard(getPlayer(controller), controller.getGame().getDeck().getLeaderCardByUuid(leaderCardUUID));
    }
}
