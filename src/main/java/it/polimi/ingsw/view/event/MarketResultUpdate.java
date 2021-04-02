package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Resource;

import java.util.List;

public class MarketResultUpdate extends PropertyUpdate {
    private String playerName;
    private List<Resource> result;

    public MarketResultUpdate(String playerName, List<Resource> result) {
        this.playerName = playerName;
        this.result = result;
    }
}
