package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.List;

public class CreateMarketUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -7140593092262570117L;

    private final List<List<Resource>> market;

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
        view.createMarket(market);
    }
}
