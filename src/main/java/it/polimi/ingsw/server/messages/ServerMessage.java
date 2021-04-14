package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.server.IServerPacket;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.io.Serializable;

public abstract class ServerMessage implements Serializable, IServerPacket {
    @Serial
    private static final long serialVersionUID = 4167105844763539403L;

    @Override
    public abstract void process(View view);
}
