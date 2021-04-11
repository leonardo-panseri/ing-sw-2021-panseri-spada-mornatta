package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.Map;

public class DepositStrongboxUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = 1526421940569412821L;

    private final String playerName;
    private final Map<Resource, Integer> strongBox;

    public DepositStrongboxUpdate(String playerName, Map<Resource, Integer> strongBox) {
        this.playerName = playerName;
        this.strongBox = strongBox;
    }

    @Override
    public void process(View view) {

    }
}
