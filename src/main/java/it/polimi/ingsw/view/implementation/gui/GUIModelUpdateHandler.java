package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.view.ModelUpdateHandler;
import it.polimi.ingsw.view.View;

public class GUIModelUpdateHandler extends ModelUpdateHandler {
    protected GUIModelUpdateHandler(View view) {
        super(view);
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        super.updateGamePhase(gamePhase);
        if (gamePhase == GamePhase.SELECTING_LEADERS) {
            GUI.instance().showPlayerBoard();
        }
    }

    @Override
    public void updateChat(String sender, String message) {
        getView().getModel().addChatMessage(sender + ": " + message );
    }
}
