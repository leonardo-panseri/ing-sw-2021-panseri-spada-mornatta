package it.polimi.ingsw.server;

import it.polimi.ingsw.view.View;

public interface IServerPacket {
    void process(View view);
}
