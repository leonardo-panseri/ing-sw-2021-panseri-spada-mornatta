package it.polimi.ingsw.server.event;

import it.polimi.ingsw.server.LobbyController;
import it.polimi.ingsw.server.ServerMessages;
import it.polimi.ingsw.server.SocketClientConnection;

import java.io.Serial;

public class PlayersToStartMessage extends ClientMessage {
    @Serial
    private static final long serialVersionUID = -3590259087689145049L;

    private int playersToStart;

    public PlayersToStartMessage(int playersToStart) {
        this.playersToStart = playersToStart;
    }

    @Override
    public void process(LobbyController lobbyController) {
        lobbyController.setPlayersToStart(getClientConnection(), playersToStart);
    }
}
