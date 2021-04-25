package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.List;
import java.util.Map;

public class DepositUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 8588565899289384713L;

    private final Map<Integer, List<Resource>> changes;
    private final Map<Resource, Integer> leadersDeposit;

    public DepositUpdate(String player, Map<Integer, List<Resource>> changes, Map<Resource, Integer> leadersDeposit) {
        super(player);
        this.changes = changes;
        this.leadersDeposit = leadersDeposit;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateDeposit(getPlayerName(), changes, leadersDeposit);
    }
}
