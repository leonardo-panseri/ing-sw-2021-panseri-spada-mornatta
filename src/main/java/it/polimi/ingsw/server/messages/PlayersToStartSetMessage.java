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

    private final int playersToStart;

    /**
     * Constructs a new PlayersToStartSetMessage.
     *
     * @param recipient the client to send this message to
     * @param playersToStart number that has been set
     */
    public PlayersToStartSetMessage(SocketClientConnection recipient, int playersToStart) {
        super(recipient);
        this.playersToStart = playersToStart;
    }

    @Override
    public void process(View view) {
        view.handleSetPlayersToStart(playersToStart);
    }
}
