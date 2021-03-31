package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Box;

public class CreateMarketUpdate extends PropertyUpdate {
    private final Box[][] market;

    public CreateMarketUpdate(Box[][] market) {
        this.market = market;
    }
}
