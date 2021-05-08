package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.server.messages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Models a lobby, holding the list of connected clients, the first connected client and the number of players needed to
 * start the game in the current lobby.
 */
public class Lobby extends Observable<IServerPacket> {
    private final UUID uuid;

    private SocketClientConnection firstConnection;
    private final List<SocketClientConnection> connections;
    private int playersToStart;
    private boolean isGameConfigSet;

    private GameConfig customGameConfig;

    /**
     * Constructs a new Lobby with a random UUID, an empty connection list and the players needed to start set to -1.
     */
    public Lobby() {
        this.uuid = UUID.randomUUID();
        this.firstConnection = null;
        this.connections = new ArrayList<>();
        this.playersToStart = -1;
        this.customGameConfig = null;
        this.isGameConfigSet = false;
    }

    /**
     * Gets the UUID of this Lobby.
     *
     * @return the uuid of the lobby
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets the list of clients connected to this lobby
     * @return the list of client connections
     */
    List<SocketClientConnection> getConnections() {
        return connections;
    }

    /**
     * Gets the custom game config for this Lobby.
     *
     * @return the custom game config for the lobby, or null if not set
     */
    public GameConfig getCustomGameConfig() {
        return customGameConfig;
    }

    /**
     * Checks if the players needed to start is set to 1.
     *
     * @return true if playersToStart = 1, false otherwise
     */
    public boolean isSinglePlayer() {
        return playersToStart == 1;
    }

    /**
     * Adds the given connection to this lobby.
     *
     * @param connection the connection to be added
     * @throws IllegalStateException if 4 clients are already connected to this lobby
     */
    public void addConnection(SocketClientConnection connection) throws IllegalStateException {
        if(connections.size() > 4)
            throw new IllegalStateException();

        connections.add(connection);

        if(firstConnection == null)
            firstConnection = connection;

        notify(new AddToLobbyMessage(connection, firstConnection == connection));
    }

    /**
     * Sets the player name for the given connection. If there is an other player with this name already connected sends
     * an error message to the client.
     *
     * @param connection the connection that will have its player name set
     * @param playerName the player name to be set, if it's null or empty sends an error message to the client
     */
    public void setPlayerName(SocketClientConnection connection, String playerName) {
        if(playerName == null || playerName.trim().equals("")) {
            notify(new ErrorMessage(connection, "Your username can't be empty"));
            return;
        }
        for(SocketClientConnection clientConnection : connections) {
            if(playerName.equalsIgnoreCase(clientConnection.getPlayerName())) {
                notify(new ErrorMessage(connection, "This username is already taken"));
                return;
            }
        }

        connection.setPlayerName(playerName);
        notify(new PlayerConnectMessage(playerName, connections.size(), playersToStart));
    }

    /**
     * Sets the players needed to start the game, if the given connection is not the first connection to the lobby
     * sends an error message to the client. If the number given is less than the number of clients already connected
     * sends an error message to the client.
     *
     * @param connection the connection that wants to set the number of players needed to start
     * @param playersToStart the number of players needed to start the game, should be between 1 and 4, otherwise an
     *                       error message will be sent to the client
     */
    public void setPlayersToStart(SocketClientConnection connection, int playersToStart) {
        if(connections.indexOf(connection) != 0) {
            notify(new ErrorMessage(connection, "Only the first player that connected to the lobby can set the number of players needed to start the game"));
            return;
        }
        if(playersToStart < 1 || playersToStart > 4) {
            notify(new ErrorMessage(connection, "This is not a number between 1 and 4"));
            return;
        }
        if(playersToStart < connections.size()) {
            notify(new ErrorMessage(connection, connections.size() + " players are already connected"));
            return;
        }

        this.playersToStart = playersToStart;
        notify(new PlayersToStartSetMessage(connection));
    }

    public void setCustomGameConfig(SocketClientConnection connection, GameConfig gameConfig) {
        if(connections.indexOf(connection) != 0) {
            notify(new ErrorMessage(connection, "Only the first player that connected to the lobby can set the number of players needed to start the game"));
            return;
        }
        if(gameConfig == null) {
            notify(new ErrorMessage(connection, "The game configuration file that you have chosen is invalid!"));
            return;
        }

        this.customGameConfig = gameConfig;
        setGameConfigSet();
        notify(new GameConfigSetMessage(connection));
    }

    /**
     * Sets to true if the master player has already chosen the desired game config
     */
    public void setGameConfigSet() {
        isGameConfigSet = true;
    }

    /**
     * Notifies all connected clients of game start.
     */
    public synchronized void startGame() {
        notify(new GameStartMessage(customGameConfig == null ? GameConfig.loadDefaultGameConfig() : customGameConfig));
    }

    /**
     * Remove the given connection from the Lobby, notifying all other clients.
     *
     * @param connection the connection to be removed from the lobby
     */
    public void disconnect(SocketClientConnection connection) {
        notify(new PlayerLeaveMessage(connection.getPlayerName()));
        connections.remove(connection);
    }

    /**
     * Remove the given crashed connection from the Lobby, disconnecting and notifying all other clients.
     *
     * @param crashedConnection the connection that crashed
     */
    public void disconnectAll(SocketClientConnection crashedConnection) {
        connections.remove(crashedConnection);

        notify(new PlayerCrashMessage(crashedConnection.getPlayerName()));
        for(SocketClientConnection conn : connections) {
            if(conn != null) {
                conn.closeConnection();
            }
            connections.remove(conn);
        }
    }

    /**
     * Checks if the number of players needed to start the game for this Lobby is set.
     *
     * @return true if 0 &lt; playersToStart &lt; 5, false otherwise
     */
    public boolean isPlayersToStartSet() {
        return playersToStart > 0 && playersToStart < 5;
    }

    /**
     * Checks if the Lobby can start a game.
     *
     * @return true if all connected clients have their name set and the number of connected clients is equal to
     *         playersToStart, false otherwise
     */
    public synchronized boolean canStart() {
        for(SocketClientConnection conn : connections)
            if(conn.getPlayerName() == null)
                return false;
        if (!isGameConfigSet) return false;
        return playersToStart == connections.size();
    }

    /**
     * Terminate the Lobby, disconnecting all clients.
     */
    public synchronized void terminate() {
        for(SocketClientConnection conn : connections) {
            if(conn != null) {
                conn.closeConnection();
            }
            connections.remove(conn);
        }
    }
}
