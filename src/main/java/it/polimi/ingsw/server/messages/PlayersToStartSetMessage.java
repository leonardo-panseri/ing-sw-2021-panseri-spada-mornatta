package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * DirectServerMessage notifying a client that the number of players to start has been set correctly.
 */
public class PlayersToStartSetMessage extends DirectServerMessage {
    @Serial
    private static final long serialVersionUID = -4320604017996942648L;

    /**
     * Constructs a new PlayersToStartSetMessage.
     *
     * @param recipient the client to send this message to
     */
    public PlayersToStartSetMessage(SocketClientConnection recipient) {
        super(recipient);
    }

    @Override
    public void process(View view) {
        view.handleSetPlayersToStart();
    }
}
