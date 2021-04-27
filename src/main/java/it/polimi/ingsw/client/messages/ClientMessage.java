package it.polimi.ingsw.client.messages;

import it.polimi.ingsw.server.LobbyController;
import it.polimi.ingsw.server.SocketClientConnection;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a message that will be sent from the Client to the Server.
 */
public abstract class ClientMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 463009486016884875L;

    private transient SocketClientConnection clientConnection;

    /**
     * Invoke the correct method in the LobbyController to handle this packet.
     *
     * @param lobbyController the lobby controller that should handle this packet
     */
    public abstract void process(LobbyController lobbyController);

    public SocketClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setClientConnection(SocketClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }
}
