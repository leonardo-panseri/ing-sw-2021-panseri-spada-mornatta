package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * ServerMessage notifying all clients of the crash of a player.
 */
public class PlayerCrashMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 9172044135400083186L;

    private final String playerName;

    /**
     * Constructs a new PlayerCrashMessage.
     *
     * @param playerName the name of the player that crashed
     */
    public PlayerCrashMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void process(View view) {
        view.handlePlayerCrash(playerName);
    }
}
