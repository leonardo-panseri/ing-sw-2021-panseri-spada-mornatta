package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
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
    private int playersToStart;
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
    }

    public synchronized void lobby(SocketClientConnection c){
        waitingConnection.add(c);

        System.out.println("Player connected: " + c.getPlayerName());

        if(waitingConnection.size() == playersToStart) {
            System.out.println("Starting game!");
            playingConnection.put(currentLobbyID, waitingConnection);
            currentLobbyID = UUID.randomUUID();

            GameController controller = new GameController();
            for(SocketClientConnection conn : waitingConnection) {
                Player player = new Player(conn.getPlayerName());
                controller.addPlayer(player);
                RemoteView remoteView = new RemoteView(player, conn);
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

            controller.getGame().getMarket().initializeMarket();
            controller.getGame().getDeck().shuffleDevelopmentDeck();
            controller.start();
            for(SocketClientConnection conn : waitingConnection) {
                conn.asyncSendServerMessage(ServerMessages.GAME_START);
            }

            waitingConnection.clear();
        }
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                if(waitingConnection.size() >= 4) {
                    newSocket.close();
                }
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this, currentLobbyID);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

}
