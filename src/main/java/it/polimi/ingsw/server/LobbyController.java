package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.event.ClientMessage;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;

public class LobbyController {
    private final Map<UUID, Lobby> playingLobbies = new HashMap<>();
    private Lobby currentLobby = new Lobby();
    private boolean currentLobbyStarting = false;

    public void update(ClientMessage message) {
        message.process(this);
    }

    public void addToLobby(SocketClientConnection connection) {
        if(currentLobbyStarting) {
            System.err.println("Trying to connect whilst game is starting, closing");

            connection.closeConnection();
            return;
        }
        currentLobby.addObserver(connection);
        try {
            currentLobby.addConnection(connection);
        } catch (IllegalStateException e) {
            System.err.println("More than 4 connections, closing this one");

            connection.closeConnection();
        }
    }

    public void setPlayerName(SocketClientConnection connection, String playerName) {
        if(connection.getPlayerName() != null) {
            return;
        }
        currentLobby.setPlayerName(connection, playerName);

        System.out.println("Player connected: " + connection.getPlayerName());

        if(currentLobby.canStart())
            startGame();
    }

    public void setPlayersToStart(SocketClientConnection connection, int playersToStart) {
        if(currentLobby.isPlayersToStartSet()) {
            return;
        }
        currentLobby.setPlayersToStart(connection, playersToStart);

        if(currentLobby.canStart())
            startGame();
    }

    private void startGame() {
        currentLobbyStarting = true;

        System.out.println("Starting game!");
        Lobby lobbyToStart = currentLobby;
        playingLobbies.put(currentLobby.getUuid(), lobbyToStart);

        GameController controller = new GameController();
        for(SocketClientConnection conn : lobbyToStart.getConnections()) {
            conn.setLobbyUUID(lobbyToStart.getUuid());

            Player player = new Player(conn.getPlayerName());
            controller.addPlayer(player);
            RemoteView remoteView = conn.getRemoteView();
            remoteView.setPlayer(player);
            remoteView.addObserver(controller);
            controller.getGame().addObserver(remoteView);
            controller.getGame().getDeck().addObserver(remoteView);
            controller.getGame().getMarket().addObserver(remoteView);
            player.addObserver(remoteView);
            player.getBoard().addObserver(remoteView);
            player.getBoard().getDeposit().addObserver(remoteView);

            if(lobbyToStart.isSinglePlayer())
                controller.getGame().createLorenzo().addObserver(remoteView);
        }

        lobbyToStart.startGame();

        controller.getGame().getMarket().initializeMarket();
        controller.getGame().getDeck().shuffleDevelopmentDeck();
        controller.getTurnController().start();

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
