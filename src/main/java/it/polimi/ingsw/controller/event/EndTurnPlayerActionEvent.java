package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;

public class EndTurnPlayerActionEvent extends PlayerActionEvent {
    @Override
    public void process(GameController controller) {
        controller.getTurnController().endTurn(getPlayer(controller));
    }
}
