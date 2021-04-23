package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.Map;

public class DepositStrongboxUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 1526421940569412821L;

    private final Map<Resource, Integer> strongBox;

    public DepositStrongboxUpdate(String playerName, Map<Resource, Integer> strongBox) {
        super(playerName);
        this.strongBox = strongBox;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateStrongbox(getPlayerName(), strongBox);
    }
}
