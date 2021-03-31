package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Resource;

import java.util.Map;

public class DepositStrongboxUpdate extends PropertyUpdate {
    private final String playerName;
    private final Map<Resource, Integer> strongBox;

    public DepositStrongboxUpdate(String playerName, Map<Resource, Integer> strongBox) {
        this.playerName = playerName;
        this.strongBox = strongBox;
    }
}
