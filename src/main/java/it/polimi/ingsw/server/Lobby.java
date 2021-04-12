package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.server.messages.*;

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

        notify(new ChooseNameMessage(connection));
    }

    public void setPlayerName(SocketClientConnection connection, String playerName) {
        if(playerName.trim().equals("")) {
            notify(new ErrorMessage(connection, "Your username can't be empty"));
            return;
        }

        connection.setPlayerName(playerName);
        notify(new PlayerConnectMessage(playerName, connections.size(), playersToStart));

        if(isFirstConnection(connection))
            notify(new ChoosePlayersToStartMessage(connection));
    }

    public void setPlayersToStart(SocketClientConnection connection, int playersToStart) {
        if(playersToStart < 1 || playersToStart > 4) {
            notify(new ErrorMessage(connection, "This is not a number between 1 and 4"));
            return;
        }
        if(playersToStart < connections.size()) {
            notify(new ErrorMessage(connection, connections.size() + " players are already connected"));
            return;
        }

        this.playersToStart = playersToStart;
    }

    public void startGame() {
        notify(new GameStartMessage());
    }

    public void disconnect(SocketClientConnection connection) {
        notify(new PlayerLeaveMessage(connection.getPlayerName()));
        connections.remove(connection);
    }

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

    public boolean isFirstConnection(SocketClientConnection connection) {
        return connection == firstConnection;
    }

    public boolean isPlayersToStartSet() {
        return playersToStart > 0 && playersToStart < 5;
    }

    public boolean canStart() {
        for(SocketClientConnection conn : connections)
            if(conn.getPlayerName() == null)
                return false;
        return playersToStart == connections.size();
    }
}
