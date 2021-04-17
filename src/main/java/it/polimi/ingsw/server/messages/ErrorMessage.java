package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.View;

import java.io.Serial;

public class ErrorMessage extends DirectServerMessage {
    @Serial
    private static final long serialVersionUID = -4465346706546640223L;

    private final String message;

    public ErrorMessage(SocketClientConnection recipient, String message) {
        super(recipient);
        this.message = message;
    }

    @Override
    public void process(View view) {
        view.getRenderer().showErrorMessage(message);
    }
}
