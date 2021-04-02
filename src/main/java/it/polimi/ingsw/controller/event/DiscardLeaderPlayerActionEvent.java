package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.card.LeaderCard;

public class DiscardLeaderPlayerActionEvent extends PlayerActionEvent {
    private LeaderCard leaderCard;

    @Override
    public void process(GameController controller) {
        controller.discardLeader(getPlayerName(), leaderCard);
    }
}
