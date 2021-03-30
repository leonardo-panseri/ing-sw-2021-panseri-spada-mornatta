package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Resource;

import java.util.List;

public class MarketUpdate extends PropertyUpdate {
    private int index;
    private List<Resource> changedList;
    private Resource slideResource;

    @Override
    public String serialize() {
        return null;
    }
}