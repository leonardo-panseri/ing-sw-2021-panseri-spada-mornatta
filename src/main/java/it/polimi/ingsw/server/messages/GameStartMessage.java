package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.View;

public class GameStartMessage extends ServerMessage {
    public GameStartMessage() {
        super("The game is starting");
    }

    @Override
    public void process(View view) {
        super.process(view);
        view.setGameState(GameState.STARTING);
    }
}
