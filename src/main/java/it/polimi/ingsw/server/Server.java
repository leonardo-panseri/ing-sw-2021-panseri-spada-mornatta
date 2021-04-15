package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(64);

    private final  LobbyController lobbyController = new LobbyController(this);

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    ExecutorService getExecutor() {
        return executor;
    }

    public void run(){
        System.out.println("Server starting...");

        while(true){
            try {
                Socket newSocket = serverSocket.accept();

                System.out.println("Accepted new connection");

                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, lobbyController);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.err.println("Connection Error!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
