package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.Map;

/**
 * Update sent after modification of the player's strongbox
 */
public class DepositStrongboxUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 1526421940569412821L;

    private final Map<Resource, Integer> strongBox;

    /**
     * Constructor: creates a new DepositStrongboxUpdate
     *
     * @param playerName nick of the owner of the strongbox
     * @param strongBox  a map that represents the strongbox
     */
    public DepositStrongboxUpdate(String playerName, Map<Resource, Integer> strongBox) {
        super(playerName);
        this.strongBox = strongBox;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateStrongbox(getPlayerName(), strongBox);
    }
}
