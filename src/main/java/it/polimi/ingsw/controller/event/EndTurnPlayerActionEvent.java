package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;

import java.io.Serial;

public class EndTurnPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = 6944619533173859281L;

    @Override
    public void process(GameController controller) {
        controller.getTurnController().endTurn(getPlayer(controller));
    }
}
