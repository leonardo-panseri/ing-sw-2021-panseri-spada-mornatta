package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;

import java.io.Serial;
import java.util.UUID;

public class DiscardLeaderPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = -5694009025273413874L;

    private final UUID leaderCardUUID;

    public DiscardLeaderPlayerActionEvent(UUID leaderCardUUID) {
        this.leaderCardUUID = leaderCardUUID;
    }

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().discardLeader(getPlayer(), getPlayer().getLeaderCardByUuid(leaderCardUUID));
    }
}
