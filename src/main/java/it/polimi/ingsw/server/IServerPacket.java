package it.polimi.ingsw.server;

import it.polimi.ingsw.IProcessablePacket;
import it.polimi.ingsw.view.View;

/**
 * Interface representing a packet that will be sent from the Server to the clients.
 */
public interface IServerPacket extends IProcessablePacket<View> {}
