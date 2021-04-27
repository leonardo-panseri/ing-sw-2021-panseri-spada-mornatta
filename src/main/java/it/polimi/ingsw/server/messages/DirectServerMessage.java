package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;

import java.io.Serial;

/**
 * Represent a message that will be sent from the Server to only one client (recipient).
 */
public abstract class DirectServerMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = -7326054893905157931L;

    private final transient SocketClientConnection recipient;

    /**
     * Constructs a new DirectServerMessage for the given recipient.
     *
     * @param recipient the client connection that this message will be sent to
     */
    DirectServerMessage(SocketClientConnection recipient) {
        this.recipient = recipient;
    }

    /**
     * Gets the recipient of this message.
     *
     * @return the client connection that is the recipient of this message
     */
    public SocketClientConnection getRecipient() {
        return recipient;
    }
}
