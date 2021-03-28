package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.Map;

public class DepositUpdate extends PropertyUpdate{
    private Player player;
    private Map<Integer, List<Resource>> changes;
}
