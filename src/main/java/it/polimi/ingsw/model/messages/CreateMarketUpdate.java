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

    /**
     * Constructor: creates a new CreateMarketUpdate
     * @param market the market, structured as a list of rows of the market
     */
    public CreateMarketUpdate(List<List<Resource>> market) {
        this.market = market;
    }

    @Override
    public String toString() {
        return "CreateMarketUpdate{" +
                "market=" + market.toString() +
                '}';
    }

    @Override
    public void process(View view) {
        view.getModel().setMarket(market);
    }
}
