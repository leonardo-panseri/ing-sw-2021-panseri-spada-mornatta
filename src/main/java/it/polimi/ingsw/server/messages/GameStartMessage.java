package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class GameStartMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 6749380507234104334L;

    @Override
    public void process(View view) {
        view.handleGameStart();
    }
}
