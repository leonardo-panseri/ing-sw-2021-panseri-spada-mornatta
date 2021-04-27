package it.polimi.ingsw.server;

import it.polimi.ingsw.view.View;

/**
 * Interface representing a packet that will be sent from the Server to the clients.
 */
public interface IServerPacket {
    /**
     * Invoke the correct method in the View to handle this packet.
     *
     * @param view the view that should handle this packet
     */
    void process(View view);
}
