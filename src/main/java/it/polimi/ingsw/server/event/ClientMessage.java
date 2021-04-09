package it.polimi.ingsw.server.event;

import it.polimi.ingsw.server.SocketClientConnection;

import java.io.Serial;
import java.io.Serializable;

public abstract class ClientMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 463009486016884875L;

    public abstract void process(SocketClientConnection connection);
}
