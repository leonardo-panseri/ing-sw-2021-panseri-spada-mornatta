package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.List;

/**
 * Update sent upon creation of the market, after starting the game.
 */

public class CreateMarketUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -7140593092262570117L;

    private final List<List<Resource>> market;
    private final Resource slideResource;

    /**
     * Constructor: creates a new CreateMarketUpdate
     * @param market the market, structured as a list of rows of the market
     * @param slideResource the resource in the slide
     */
    public CreateMarketUpdate(List<List<Resource>> market, Resource slideResource) {
        this.market = market;
        this.slideResource = slideResource;
    }

    @Override
    public void process(View view) {
        view.getModel().getMarket().setGrid(market);
        view.getModel().getMarket().setSlideResource(slideResource);
    }
}
