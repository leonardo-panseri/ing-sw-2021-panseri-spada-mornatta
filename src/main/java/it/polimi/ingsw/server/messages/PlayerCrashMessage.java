package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class PlayerCrashMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 9172044135400083186L;

    private final String playerName;

    public PlayerCrashMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void process(View view) {
        view.handlePlayerCrash(playerName);
    }
}
