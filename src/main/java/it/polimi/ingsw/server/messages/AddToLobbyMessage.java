package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.View;

import java.io.Serial;

public class AddToLobbyMessage extends DirectServerMessage {
    @Serial
    private static final long serialVersionUID = -695696550449639574L;

    private final boolean firstConnection;

    public AddToLobbyMessage(SocketClientConnection recipient, boolean firstConnection) {
        super(recipient);
        this.firstConnection = firstConnection;
    }

    @Override
    public void process(View view) {
        view.addToLobby(firstConnection);
    }
}
