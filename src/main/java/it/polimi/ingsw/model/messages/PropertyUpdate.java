package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.server.IServerPacket;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.io.Serializable;

/**
 * Update sent upon modification of one property of the game.
 * It is sent by the server to the clients, and then processed locally using the selected views to show the changes.
 */
public abstract class PropertyUpdate implements Serializable, IServerPacket {
    @Serial
    private static final long serialVersionUID = 8611410949820701833L;
}