package it.polimi.ingsw.server.event;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.LobbyController;
import it.polimi.ingsw.server.SocketClientConnection;

import java.io.Serial;
import java.io.Serializable;

public abstract class ClientMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 463009486016884875L;

    private transient SocketClientConnection clientConnection;

    public abstract void process(LobbyController lobbyController);

    public SocketClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setClientConnection(SocketClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }
}
