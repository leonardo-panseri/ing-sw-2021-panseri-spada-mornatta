package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Resource;

import java.util.List;

public class MarketUpdate extends PropertyUpdate {
    private int index;
    private List<Resource> changedList;
    private Resource slideResource;

    public MarketUpdate(int index, List<Resource> changedList, Resource slideResource) {
        this.index = index;
        this.changedList = changedList;
        this.slideResource = slideResource;
    }
}