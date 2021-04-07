package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;

import java.util.List;

public class MarketPlayerActionEvent extends PlayerActionEvent {
    private int selected;
    private Resource whiteConversion;

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
