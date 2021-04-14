package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;

import java.io.Serial;

public class MarketPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = 1928950514602330030L;

    private int selected;
    private Resource whiteConversion;

    public MarketPlayerActionEvent(String playerName, int selected, Resource whiteConversion) {
        super(playerName);
        this.selected = selected;
        this.whiteConversion = whiteConversion;
    }

    public int getSelected() {
        return selected;
    }

    public Resource getWhiteConversion() {
        return whiteConversion;
    }

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().useMarket(getPlayer(controller), selected, whiteConversion);
    }
}
