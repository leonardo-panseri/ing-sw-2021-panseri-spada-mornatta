package it.polimi.ingsw.client.messages;

import it.polimi.ingsw.server.LobbyController;

import java.io.Serial;

public class PlayerNameMessage extends ClientMessage {
    @Serial
    private static final long serialVersionUID = 7401855340201090917L;

    private String playerName;

    public PlayerNameMessage(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void process(LobbyController lobbyController) {
        lobbyController.setPlayerName(getClientConnection(), playerName);
    }
}
