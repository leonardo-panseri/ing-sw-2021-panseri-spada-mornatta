package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * Update sent after a modification of the player's deposit.
 */
public class DepositUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 8588565899289384713L;

    private final Map<Integer, List<Resource>> changes;
    private final Map<Integer, List<Resource>> leadersDeposit;

    /**
     * Constructor: creates a new DepositUpdate
     * @param player nick of the owner of the deposit
     * @param changes a map where the Integer is the index of a changed row, and the List is the new row of the deposit
     * @param leadersDeposit a map that represents the possible deposits granted by some leaders
     */
    public DepositUpdate(String player, Map<Integer, List<Resource>> changes, Map<Integer, List<Resource>> leadersDeposit) {
        super(player);
        this.changes = changes;
        this.leadersDeposit = leadersDeposit;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateDeposit(getPlayerName(), changes, leadersDeposit);
    }
}
