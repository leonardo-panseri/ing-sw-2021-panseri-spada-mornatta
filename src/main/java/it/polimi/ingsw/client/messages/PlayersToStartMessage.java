package it.polimi.ingsw.client.messages;

import it.polimi.ingsw.server.LobbyController;

import java.io.Serial;

/**
 * ClientMessage notifying the server of the chosen number of players needed to start the game.
 */
public class PlayersToStartMessage extends ClientMessage {
    @Serial
    private static final long serialVersionUID = -3590259087689145049L;

    private final int playersToStart;

    /**
     * Constructs a new PlayersToStartMessage.
     *
     * @param playersToStart  the number of players needed to start the game that the client has chosen
     */
    public PlayersToStartMessage(int playersToStart) {
        this.playersToStart = playersToStart;
    }

    @Override
    public void process(LobbyController lobbyController) {
        lobbyController.setPlayersToStart(getClientConnection(), playersToStart);
    }
}
