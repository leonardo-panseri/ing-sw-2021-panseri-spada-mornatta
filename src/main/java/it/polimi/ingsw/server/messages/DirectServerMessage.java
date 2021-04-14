package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;

import java.io.Serial;

public abstract class DirectServerMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = -7326054893905157931L;

    private final transient SocketClientConnection recipient;

    DirectServerMessage(SocketClientConnection recipient) {
        this.recipient = recipient;
    }

    public SocketClientConnection getRecipient() {
        return recipient;
    }
}
