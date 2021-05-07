package it.polimi.ingsw.server;

import it.polimi.ingsw.client.messages.ClientMessage;

import java.util.*;

/**
 * Controller for the lobbies waiting to start a game.
 */
public class LobbyController {
    private final Server server;
    private final Map<UUID, Lobby> playingLobbies = new HashMap<>();
    private Lobby currentLobby = new Lobby();

    /**
     * Constructs a new LobbyController for the given Server.
     *
     * @param server the server that this LobbyController belongs to
     */
    LobbyController(Server server) {
        this.server = server;
    }

    /**
     * Processes the given ClientMessage.
     *
     * @param message the client message to process
     */
    public synchronized void update(ClientMessage message) {
        message.process(this);
    }

    /**
     * Adds the given ClientConnection to the current lobby.
     *
     * @param connection the connection that will be added to the lobby
     */
    public synchronized void addToLobby(SocketClientConnection connection) {
        currentLobby.addObserver(connection.getRemoteView());
        try {
            currentLobby.addConnection(connection);
        } catch (IllegalStateException e) {
            System.err.println("More than 4 connections, closing this one");

            connection.closeConnection();
        }
    }

    /**
     * Sets the name of the Player that is associated with the given ClientConnection.
     *
     * @param connection the connection that will have its player name set
     * @param playerName the name to be set
     */
    public synchronized void setPlayerName(SocketClientConnection connection, String playerName) {
        if(connection.getPlayerName() != null) {
            return;
        }
        currentLobby.setPlayerName(connection, playerName);

        System.out.println("Player connected: " + connection.getPlayerName());

        if(currentLobby.canStart())
            startGame();
    }

    /**
     * Sets the number of players needed to start the game in the current lobby.
     *
     * @param connection the connection that wants to set the players needed to start
     * @param playersToStart the number of players needed to start the game in the current lobby
     */
    public synchronized void setPlayersToStart(SocketClientConnection connection, int playersToStart) {
        if(currentLobby.isPlayersToStartSet()) {
            return;
        }
        currentLobby.setPlayersToStart(connection, playersToStart);
    }

    public synchronized void setGameConfig(SocketClientConnection connection, String serializedGameConfig) {
        if(serializedGameConfig == null) {
            if(currentLobby.canStart())
                startGame();
            return;
        }

        GameConfig gameConfig = GameConfig.deserialize(serializedGameConfig);

        currentLobby.setCustomGameConfig(connection, gameConfig);

        if(currentLobby.canStart())
            startGame();
    }

    /**
     * Starts the game in the current lobby. The game is initialized in a separate Thread to allow new players to connect
     * to the next lobby without waiting.
     */
    private synchronized void startGame() {
        Lobby lobbyToStart = currentLobby;
        playingLobbies.put(currentLobby.getUuid(), lobbyToStart);

        server.getExecutor().submit(new GameInstance(lobbyToStart));

        currentLobby = new Lobby();
    }

    /**
     * Deregister a connection from the lobby, if it is in a playing lobby terminates the game and disconnects all other
     * players in that lobby.
     *
     * @param connection the connection to deregister
     */
    public synchronized void deregisterConnection(SocketClientConnection connection) {
        if(connection.getLobbyUUID() == null) {
            currentLobby.disconnect(connection);
        } else {
            Lobby lobby = playingLobbies.get(connection.getLobbyUUID());
            if(lobby != null) {
                lobby.disconnectAll(connection);

                playingLobbies.remove(connection.getLobbyUUID());
            }
        }
    }
}
