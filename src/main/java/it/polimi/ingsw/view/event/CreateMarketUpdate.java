package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Box;

import java.util.Arrays;

public class CreateMarketUpdate extends PropertyUpdate {
    private final Box[][] market;

    public CreateMarketUpdate(Box[][] market) {
        this.market = market;
    }

    @Override
    public String toString() {
        return "CreateMarketUpdate{" +
                "market=" + Arrays.toString(market) +
                '}';
    }
}
