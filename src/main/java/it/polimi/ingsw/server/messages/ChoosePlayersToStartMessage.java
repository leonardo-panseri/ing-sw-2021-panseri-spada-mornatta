package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.View;

public class ChoosePlayersToStartMessage extends DirectServerMessage {
    public ChoosePlayersToStartMessage(SocketClientConnection recipient) {
        super(recipient, "Choose the number of players required to start the game:");
    }

    @Override
    public void process(View view) {
        super.process(view);
        view.setGameState(GameState.CHOOSING_PLAYERS);
    }
}
