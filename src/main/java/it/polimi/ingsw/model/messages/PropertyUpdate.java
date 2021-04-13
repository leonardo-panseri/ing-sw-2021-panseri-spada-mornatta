package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.server.IServerPacket;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.io.Serializable;

public abstract class PropertyUpdate implements Serializable, IServerPacket {
    @Serial
    private static final long serialVersionUID = 8611410949820701833L;

    @Override
    public abstract void process(View view);
}