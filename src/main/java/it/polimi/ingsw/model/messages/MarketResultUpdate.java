package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.List;

/**
 * Update sent to the player that has drawn from the market. Contains the result.
 */
public class MarketResultUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 5345288716406897205L;

    private final List<Resource> result;

    /**
     * Constructor: creates a new MarketResultUpdate.
     *
     * @param playerName the player that drew from the market
     * @param result     a list of resource drawn from the market
     **/
    public MarketResultUpdate(String playerName, List<Resource> result) {
        super(playerName);
        this.result = result;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().insertDrawnResources(getPlayerName(), result);
    }
}
