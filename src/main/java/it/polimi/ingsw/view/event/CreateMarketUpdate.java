package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Box;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.Arrays;

public class CreateMarketUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -7140593092262570117L;

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

    @Override
    public void process(View view) {

    }
}
