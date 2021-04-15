package it.polimi.ingsw.server;

import it.polimi.ingsw.client.messages.ClientMessage;

import java.util.*;

public class LobbyController {
    private final Server server;
    private final Map<UUID, Lobby> playingLobbies = new HashMap<>();
    private Lobby currentLobby = new Lobby();
    private boolean currentLobbyStarting = false;

    LobbyController(Server server) {
        this.server = server;
    }

    public synchronized void update(ClientMessage message) {
        message.process(this);
    }

    public synchronized void addToLobby(SocketClientConnection connection) {
        if(currentLobbyStarting) {
            System.err.println("Trying to connect whilst game is starting, closing");

            connection.closeConnection();
            return;
        }
        currentLobby.addObserver(connection.getRemoteView());
        try {
            currentLobby.addConnection(connection);
        } catch (IllegalStateException e) {
            System.err.println("More than 4 connections, closing this one");

            connection.closeConnection();
        }
    }

    public synchronized void setPlayerName(SocketClientConnection connection, String playerName) {
        if(connection.getPlayerName() != null) {
            return;
        }
        currentLobby.setPlayerName(connection, playerName);

        System.out.println("Player connected: " + connection.getPlayerName());

        if(currentLobby.canStart())
            startGame();
    }

    public synchronized void setPlayersToStart(SocketClientConnection connection, int playersToStart) {
        if(currentLobby.isPlayersToStartSet()) {
            return;
        }
        currentLobby.setPlayersToStart(connection, playersToStart);

        if(currentLobby.canStart())
            startGame();
    }

    private synchronized void startGame() {
        currentLobbyStarting = true;

        Lobby lobbyToStart = currentLobby;
        playingLobbies.put(currentLobby.getUuid(), lobbyToStart);

        server.getExecutor().submit(new GameInstance(lobbyToStart));

        currentLobby = new Lobby();

        currentLobbyStarting = false;
    }

    public synchronized void deregisterConnection(SocketClientConnection connection) {
        if(connection.getLobbyUUID() == null) {
            if(currentLobbyStarting)
                currentLobby.disconnectAll(connection);
            else
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
