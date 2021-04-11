package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.server.event.DirectServerMessage;
import it.polimi.ingsw.server.event.ServerMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby extends Observable<ServerMessage> {
    private final UUID uuid;

    private SocketClientConnection firstConnection;
    private final List<SocketClientConnection> connections;
    private int playersToStart;

    public Lobby() {
        this.uuid = UUID.randomUUID();
        this.firstConnection = null;
        this.connections = new ArrayList<>();
        this.playersToStart = -1;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<SocketClientConnection> getConnections() {
        return connections;
    }

    public boolean isSinglePlayer() {
        return playersToStart == 1;
    }

    public void addConnection(SocketClientConnection connection) throws IllegalStateException {
        if(connections.size() > 4)
            throw new IllegalStateException();

        connections.add(connection);

        if(firstConnection == null)
            firstConnection = connection;

        notify(new DirectServerMessage(connection, ServerMessages.INPUT_NAME));
    }

    public void setPlayerName(SocketClientConnection connection, String playerName) {
        if(playerName.trim().equals("")) {
            notify(new DirectServerMessage(connection, ServerMessages.INVALID_INPUT));
            return;
        }

        connection.setPlayerName(playerName);

        if(isFirstConnection(connection))
            notify(new DirectServerMessage(connection, ServerMessages.CHOOSE_PLAYER_NUM));
    }

    public void setPlayersToStart(SocketClientConnection connection, int playersToStart) {
        if(playersToStart < 1 || playersToStart > 4) {
            notify(new DirectServerMessage(connection, ServerMessages.INVALID_INPUT));
            return;
        }
        if(playersToStart < connections.size()) {
            notify(new DirectServerMessage(connection, ServerMessages.TOO_MANY_CONNECTIONS));
            return;
        }

        this.playersToStart = playersToStart;
    }

    public void startGame() {
        notify(new ServerMessage(ServerMessages.GAME_START));
    }

    public void disconnect(SocketClientConnection connection) {
        //notify();
        connections.remove(connection);
    }

    public void disconnectAll(SocketClientConnection crashedConnection) {
        connections.remove(crashedConnection);

        notify(new ServerMessage(ServerMessages.SOMEONE_CRASHED));
        for(SocketClientConnection conn : connections) {
            if(conn != null) {
                conn.closeConnection();
            }
            connections.remove(conn);
        }
    }

    public boolean isFirstConnection(SocketClientConnection connection) {
        return connection == firstConnection;
    }

    public boolean isPlayersToStartSet() {
        return playersToStart > 0 && playersToStart < 5;
    }

    public boolean canStart() {
        return playersToStart == connections.size();
    }
}
