package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.view.View;

/**
 * Update sent after a game phase change
 */
public class GamePhaseUpdate extends PropertyUpdate {
    private final GamePhase currentPhase;

    /**
     * Constructor: creates a new GamePhaseUpdate.
     * @param currentPhase the new current phase of the game
     * */
    public GamePhaseUpdate(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateGamePhase(currentPhase);
    }
}
