package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.View;

import java.io.Serial;

public class PlayersToStartSetMessage extends DirectServerMessage {
    @Serial
    private static final long serialVersionUID = -4320604017996942648L;

    public PlayersToStartSetMessage(SocketClientConnection recipient) {
        super(recipient);
    }

    @Override
    public void process(View view) {
        view.handleSetPlayersToStart();
    }
}
