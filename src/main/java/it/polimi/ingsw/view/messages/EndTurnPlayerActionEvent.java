package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;

import java.io.Serial;

public class EndTurnPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = 6944619533173859281L;

    public EndTurnPlayerActionEvent(String playerName) {
        super(playerName);
    }

    @Override
    public void process(GameController controller) {
        controller.getTurnController().endTurn(getPlayer(controller));
    }
}
