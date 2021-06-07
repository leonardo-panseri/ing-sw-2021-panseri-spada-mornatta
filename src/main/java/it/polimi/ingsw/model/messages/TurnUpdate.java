package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * Update sent when the turn shifts towards the next player in the playing order.
 */
public class TurnUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -6728483798081190281L;

    private final String currentPlayerName;

    /**
     * Constructor: creates a new TurnUpdate.
     *
     * @param currentPlayerName the current playing player
     **/
    public TurnUpdate(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateTurn(currentPlayerName);
    }
}
