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
    private final Map<SocketClientConnection, Boolean> waitingConnection = new HashMap<>();
    private final Map<UUID, Set<SocketClientConnection>> playingConnection = new HashMap<>();
    private UUID currentLobbyID = UUID.randomUUID();

    public synchronized void deregisterConnection(SocketClientConnection c) {
        Set<SocketClientConnection> connections = playingConnection.get(c.getLobbyID());
        if(connections != null) {
            connections.remove(c);
            for(SocketClientConnection conn : connections) {
                if(conn != null) {
                    conn.closeConnection();
                }
                connections.remove(conn);
            }
            playingConnection.remove(c.getLobbyID());
        } else {
            waitingConnection.remove(c);
        }
    }

    public synchronized void lobby(SocketClientConnection c){
        waitingConnection.put(c, false);
        c.asyncSend(ServerMessages.LOBBY_WAITING);
    }

    public synchronized void ready(SocketClientConnection c) {
        waitingConnection.put(c, true);

        boolean startGame = true;
        for(boolean b : waitingConnection.values())
            startGame = startGame && b;

        if(startGame) {
            Set<SocketClientConnection> connections = waitingConnection.keySet();
            playingConnection.put(currentLobbyID, connections);
            waitingConnection.clear();

            GameController controller = new GameController();
            for(SocketClientConnection conn : connections) {
                Player player = new Player(conn.getPlayerName());
                controller.addPlayer(player);
                RemoteView remoteView = new RemoteView(player, conn);
                //controller.addObserver(remoteView);
                //forse model?
                remoteView.addObserver(controller);
            }
        }
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                if(waitingConnection.keySet().size() > 4) {
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
