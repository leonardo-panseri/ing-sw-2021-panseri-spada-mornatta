package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final List<SocketClientConnection> waitingConnection = new ArrayList<>();
    private int playersToStart = -1;
    private final Map<UUID, List<SocketClientConnection>> playingConnection = new HashMap<>();
    private UUID currentLobbyID = UUID.randomUUID();

    public synchronized void deregisterConnection(SocketClientConnection c) {
        List<SocketClientConnection> connections = playingConnection.get(c.getLobbyID());
        if(connections != null) {
            connections.remove(c);
            for(SocketClientConnection conn : connections) {
                if(conn != null) {
                    conn.sendServerMessage(ServerMessages.SOMEONE_CRASHED);
                    conn.closeConnection();
                }
                connections.remove(conn);
            }
            playingConnection.remove(c.getLobbyID());
        } else {
            waitingConnection.remove(c);
        }
    }

    public boolean isLobbyEmpty() {
        return waitingConnection.isEmpty();
    }

    void setPlayersToStart(int playersToStart) {
        this.playersToStart = playersToStart;
        checkGameStart();
    }

    public synchronized void lobby(SocketClientConnection c){
        waitingConnection.add(c);

        System.out.println("Player connected: " + c.getPlayerName());

        checkGameStart();
    }

    private void checkGameStart() {
        if(waitingConnection.size() == playersToStart) {
            System.out.println("Starting game!");
            playingConnection.put(currentLobbyID, waitingConnection);
            currentLobbyID = UUID.randomUUID();

            GameController controller = new GameController();
            for(SocketClientConnection conn : waitingConnection) {
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
                if(playersToStart == 1) {
                    controller.getGame().createLorenzo().addObserver(remoteView);
                }
            }

            for(SocketClientConnection conn : waitingConnection) {
                conn.sendServerMessage(ServerMessages.GAME_START);
            }

            controller.getGame().getMarket().initializeMarket();
            controller.getGame().getDeck().shuffleDevelopmentDeck();
            controller.getTurnController().start();

            waitingConnection.clear();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    playersToStart = -1;
                }
            }, 200);
        }
    }

    public boolean isPlayersToStartSet() {
        return playersToStart > 0 && playersToStart < 5;
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        System.out.println("Server starting...");

        while(true){
            try {
                System.out.println("Waiting for connection");

                Socket newSocket = serverSocket.accept();

                System.out.println("Accepted new connection");

                if(waitingConnection.size() >= 4) {
                    newSocket.close();

                    System.out.println("Can't accept more than 4 connections");
                }
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this, currentLobbyID);
                socketConnection.setRemoteView(new RemoteView(socketConnection));
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

}
