package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * DirectServerMessage notifying a client of its successful connection.
 */
public class AddToLobbyMessage extends DirectServerMessage {
    @Serial
    private static final long serialVersionUID = -695696550449639574L;

    private final boolean firstConnection;

    /**
     * Constructs a new AddToLobbyMessage for the given client connection.
     *
     * @param recipient       the client connection to send this message to
     * @param firstConnection true if the client is the first to connect to the lobby, false otherwise
     */
    public AddToLobbyMessage(SocketClientConnection recipient, boolean firstConnection) {
        super(recipient);
        this.firstConnection = firstConnection;
    }

    @Override
    public void process(View view) {
        view.addToLobby(firstConnection);
    }
}
