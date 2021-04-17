package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class TurnUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -6728483798081190281L;

    private final String currentPlayerName;

    public TurnUpdate(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateTurn(currentPlayerName);
    }
}
