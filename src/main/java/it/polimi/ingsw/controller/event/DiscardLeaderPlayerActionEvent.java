package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.UUID;

public class DiscardLeaderPlayerActionEvent extends PlayerActionEvent {
    private UUID leaderCardUUID;

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().discardLeader(getPlayer(controller), controller.getGame().getDeck().getLeaderCardByUuid(leaderCardUUID));
    }
}
