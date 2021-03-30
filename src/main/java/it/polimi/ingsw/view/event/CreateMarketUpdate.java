package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Box;

public class CreateMarketUpdate extends PropertyUpdate {
    private Box[][] market;

    public CreateMarketUpdate(Box[][] market) {
        this.market = market;
    }

    @Override
    public String serialize() {
        return null;
    }
}
