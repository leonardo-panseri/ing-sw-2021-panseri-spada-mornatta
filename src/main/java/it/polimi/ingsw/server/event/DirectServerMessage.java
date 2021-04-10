package it.polimi.ingsw.server.event;

import it.polimi.ingsw.server.SocketClientConnection;

public class DirectServerMessage extends ServerMessage {
    private final transient SocketClientConnection recipient;

    public DirectServerMessage(SocketClientConnection recipient, String message) {
        super(message);
        this.recipient = recipient;
    }

    public SocketClientConnection getRecipient() {
        return recipient;
    }
}
