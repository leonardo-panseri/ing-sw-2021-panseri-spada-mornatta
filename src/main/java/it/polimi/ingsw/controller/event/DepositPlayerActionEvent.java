package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;

import java.util.List;
import java.util.Map;

public class DepositPlayerActionEvent extends PlayerActionEvent {
    private final Map<Integer, List<Resource>> changes; // The model should check that changes are legal

    public DepositPlayerActionEvent(Map<Integer, List<Resource>> changes) {
        this.changes = changes;
    }

    @Override
    public void process(GameController controller) {
        controller.updatePlayerDeposit(getPlayerName(), changes);
    }
}
