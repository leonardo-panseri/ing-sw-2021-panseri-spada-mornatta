package it.polimi.ingsw.client.messages;

import it.polimi.ingsw.server.LobbyController;

import java.io.Serial;

/**
 * ClientMessage notifying the server of the chosen username.
 */
public class PlayerNameMessage extends ClientMessage {
    @Serial
    private static final long serialVersionUID = 7401855340201090917L;

    private final String playerName;

    /**
     * Constructs a new PlayerNameMessage.
     *
     * @param playerName the player name that the client has chosen
     */
    public PlayerNameMessage(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void process(LobbyController lobbyController) {
        lobbyController.setPlayerName(getClientConnection(), playerName);
    }
}
