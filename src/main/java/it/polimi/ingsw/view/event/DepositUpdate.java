package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.List;
import java.util.Map;

public class DepositUpdate extends PropertyUpdate{
    @Serial
    private static final long serialVersionUID = 8588565899289384713L;

    private final String player;
    private final Map<Integer, List<Resource>> changes;

    public DepositUpdate(String player, Map<Integer, List<Resource>> changes) {
        this.player = player;
        this.changes = changes;
    }

    @Override
    public void process(View view) {

    }
}
