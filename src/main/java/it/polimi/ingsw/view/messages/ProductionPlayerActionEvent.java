package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.view.messages.production.Production;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ProductionPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = -1555283945398296364L;

    private final List<Production> productions;

    public ProductionPlayerActionEvent(List<Production> productions) {
        this.productions = new ArrayList<>(productions);
    }

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().useProductions(getPlayer(), productions);
    }
}
