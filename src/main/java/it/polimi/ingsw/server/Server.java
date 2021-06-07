package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main server instance.
 */
public class Server {
    private static final int PORT = 12345;

    private final ServerSocket serverSocket;

    private final LobbyController lobbyController;

    /**
     * Constructs a new Server, initializing the ServerSocket, the FixedThreadPool and the LobbyController.
     *
     * @throws IOException if an error occurs while initializing the ServerSocket
     */
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.lobbyController = new LobbyController();
    }

    /**
     * Runs the server and makes it wait for and accept incoming connections.
     */
    public void run() {
        System.out.println("Server starting...");

        while (true) {
            try {
                Socket newSocket = serverSocket.accept();

                System.out.println("Accepted new connection");

                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, lobbyController);
                Thread t = new Thread(socketConnection);
                t.start();
            } catch (IOException e) {
                System.err.println("Connection Error!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
