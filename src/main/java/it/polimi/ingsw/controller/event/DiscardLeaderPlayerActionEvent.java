package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.card.LeaderCard;

import java.io.Serial;
import java.util.UUID;

public class DiscardLeaderPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = -5694009025273413874L;

    private UUID leaderCardUUID;

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().discardLeader(getPlayer(controller), controller.getGame().getDeck().getLeaderCardByUuid(leaderCardUUID));
    }
}
