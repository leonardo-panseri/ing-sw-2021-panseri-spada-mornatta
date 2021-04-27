package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;

import java.io.Serial;
import java.util.List;

public class MarketPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = 1928950514602330030L;

    private final int selected;
    private final List<Resource> whiteConversions;

    public MarketPlayerActionEvent(int selected, List<Resource> whiteConversions) {
        this.selected = selected;
        this.whiteConversions = whiteConversions;
    }

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().useMarket(getPlayer(), selected, whiteConversions);
    }
}
