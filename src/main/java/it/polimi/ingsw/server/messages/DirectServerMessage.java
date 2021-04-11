package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;

public abstract class DirectServerMessage extends ServerMessage {
    private final transient SocketClientConnection recipient;

    DirectServerMessage(SocketClientConnection recipient, String message) {
        super(message);
        this.recipient = recipient;
    }

    public SocketClientConnection getRecipient() {
        return recipient;
    }
}
