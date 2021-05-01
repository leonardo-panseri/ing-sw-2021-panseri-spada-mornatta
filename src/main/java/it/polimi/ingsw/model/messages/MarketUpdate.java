package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.List;

/**
 * Update sent upon modification of the market.
 */
public class MarketUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -1062992561986996661L;

    private final int index;
    private final List<Resource> changedList;
    private final Resource slideResource;

    /**
     * Constructor: creates a new MarketUpdate.
     * @param index the row/column drawn by the player
     * @param changedList new list of the resources replacing the old one
     * @param slideResource the new slide resource
     **/
    public MarketUpdate(int index, List<Resource> changedList, Resource slideResource) {
        this.index = index;
        this.changedList = changedList;
        this.slideResource = slideResource;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateMarket(index, changedList, slideResource);
    }
}