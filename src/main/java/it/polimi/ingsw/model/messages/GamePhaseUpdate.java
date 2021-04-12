package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.view.View;

public class GamePhaseUpdate extends PropertyUpdate {
    private final GamePhase currentPhase;

    public GamePhaseUpdate(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    @Override
    public void process(View view) {
        view.updateGamePhase(currentPhase);
    }
}
