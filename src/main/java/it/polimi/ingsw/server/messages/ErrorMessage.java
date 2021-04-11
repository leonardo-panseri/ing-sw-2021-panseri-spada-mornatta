package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;

public class ErrorMessage extends DirectServerMessage {
    public ErrorMessage(SocketClientConnection recipient, String message) {
        super(recipient, message);
    }
}
