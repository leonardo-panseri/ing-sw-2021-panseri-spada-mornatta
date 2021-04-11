package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.View;

public class ChooseNameMessage extends DirectServerMessage {
    public ChooseNameMessage(SocketClientConnection recipient) {
        super(recipient, "Choose a username:");
    }

    @Override
    public void process(View view) {
        super.process(view);
        view.setGameState(GameState.CHOOSING_NAME);
    }
}
