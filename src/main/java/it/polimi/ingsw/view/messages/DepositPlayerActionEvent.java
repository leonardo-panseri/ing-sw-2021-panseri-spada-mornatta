package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;

import java.io.Serial;
import java.util.List;
import java.util.Map;

public class DepositPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = 9204228336536058886L;

    private final Map<Integer, List<Resource>> changes;
    private final List<Resource> toBeStored;
    private final Map<Resource, Integer> leadersDeposit;

    public DepositPlayerActionEvent(String playerName, Map<Integer, List<Resource>> changes, List<Resource> toBeStored, Map<Resource, Integer> leadersDeposit) {
        super(playerName);
        this.changes = changes;
        this.toBeStored = toBeStored;
        this.leadersDeposit = leadersDeposit;
    }

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().updatePlayerDeposit(getPlayer(controller), changes, toBeStored, leadersDeposit);
    }
}
