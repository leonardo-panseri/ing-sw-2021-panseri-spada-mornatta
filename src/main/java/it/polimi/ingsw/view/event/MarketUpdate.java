package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.List;

public class MarketUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -1062992561986996661L;

    private int index;
    private List<Resource> changedList;
    private Resource slideResource;

    public MarketUpdate(int index, List<Resource> changedList, Resource slideResource) {
        this.index = index;
        this.changedList = changedList;
        this.slideResource = slideResource;
    }

    @Override
    public void process(View view) {

    }
}