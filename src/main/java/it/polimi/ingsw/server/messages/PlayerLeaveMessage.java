package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class PlayerLeaveMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 54329163086061406L;

    private final String playerName;

    public PlayerLeaveMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void process(View view) {
        view.handlePlayerDisconnect(playerName);
    }
}
