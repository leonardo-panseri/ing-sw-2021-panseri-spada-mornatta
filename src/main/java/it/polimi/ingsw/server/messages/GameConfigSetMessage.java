package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.View;

import java.io.Serial;

public class GameConfigSetMessage extends DirectServerMessage {
    @Serial
    private static final long serialVersionUID = 6337210376928226127L;

    /**
     * Constructs a new DirectServerMessage for the given recipient.
     *
     * @param recipient the client connection that this message will be sent to
     */
    public GameConfigSetMessage(SocketClientConnection recipient) {
        super(recipient);
    }

    @Override
    public void process(View controller) {
        controller.handleSetGameConfig();
    }
}
