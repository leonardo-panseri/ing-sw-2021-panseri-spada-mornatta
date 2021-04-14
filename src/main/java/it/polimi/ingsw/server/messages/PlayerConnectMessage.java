package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class PlayerConnectMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = -8678594154824429984L;

    private final String playerName;
    private final int currentPlayers;
    private final int playersToStart;

    public PlayerConnectMessage(String playerName, int currentPlayers, int playersToStart) {
        this.playerName = playerName;
        this.currentPlayers = currentPlayers;
        this.playersToStart = playersToStart;
    }

    @Override
    public void process(View view) {
        view.handlePlayerConnect(playerName, currentPlayers, playersToStart);
    }
}
