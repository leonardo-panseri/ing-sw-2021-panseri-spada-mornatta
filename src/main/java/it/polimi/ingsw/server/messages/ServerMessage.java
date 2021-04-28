package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.IServerPacket;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a message that will be sent from the Server to the clients.
 */
public abstract class ServerMessage implements Serializable, IServerPacket {
    @Serial
    private static final long serialVersionUID = 4167105844763539403L;
}
