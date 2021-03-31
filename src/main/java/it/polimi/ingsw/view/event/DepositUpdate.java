package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Resource;

import java.util.List;
import java.util.Map;

public class DepositUpdate extends PropertyUpdate{
    private final String player;
    private final Map<Integer, List<Resource>> changes;

    public DepositUpdate(String player, Map<Integer, List<Resource>> changes) {
        this.player = player;
        this.changes = changes;
    }
}
