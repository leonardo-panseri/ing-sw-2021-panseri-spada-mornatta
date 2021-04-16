package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main server instance.
 */
public class Server {
    private static final int PORT = 12345;
    private static final int THREADS_NUM = 64;

    private final ServerSocket serverSocket;
    private final ExecutorService executor;

    private final  LobbyController lobbyController;

    /**
     * Constructs a new Server, initializing the ServerSocket, the FixedThreadPool and the LobbyController.
     *
     * @throws IOException if an error occurs while initializing the ServerSocket
     */
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.executor = Executors.newFixedThreadPool(THREADS_NUM);
        this.lobbyController = new LobbyController(this);
    }

    /**
     * Gets the FixedThreadPool executor.
     *
     * @return the executor service
     */
    ExecutorService getExecutor() {
        return executor;
    }

    /**
     * Runs the server and makes it wait for and accept incoming connections.
     */
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
