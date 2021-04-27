package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Runnable class representing a client connection handler.
 */
public class SocketClientConnection implements Runnable {
    private final Socket socket;
    private WriteThread writeThread;

    private String playerName;
    private final RemoteView remoteView;
    private UUID lobbyUUID;

    private boolean active = true;

    /**
     * Creates a new SocketClientConnection that manages the communication with the given Socket.
     *
     * @param socket the client socket
     * @param lobbyController the main lobby controller
     */
    SocketClientConnection(Socket socket, LobbyController lobbyController) {
        this.socket = socket;
        this.playerName = null;
        this.remoteView = new RemoteView(this, lobbyController);
        this.lobbyUUID = null;
    }

    /**
     * Checks if this instance is still running.
     *
     * @return true if the instance is still running, false otherwise
     */
    synchronized boolean isActive(){
        return active;
    }

    /**
     * Sets this instance as no longer running.
     */
    synchronized void setInactive() {
        this.active = false;
    }

    /**
     * Gets the player name associated with this connection.
     *
     * @return the player name, or <code>null</code> if it has not been set yet
     */
    public synchronized String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the player name associated with this connection.
     *
     * @param playerName the player name to associate with this connection
     */
    public synchronized void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the RemoteView associated with this connection.
     *
     * @return the remote view associated with this connection
     */
    public RemoteView getRemoteView() {
        return remoteView;
    }

    /**
     * Gets the UUID of the Lobby that this connection is part of.
     *
     * @return the uuid of the lobby
     */
    public synchronized UUID getLobbyUUID() {
        return lobbyUUID;
    }

    /**
     * Sets the UUID of the Lobby that this connection is part of.
     *
     * @param lobbyUUID the uuid of the lobby
     */
    public synchronized void setLobbyUUID(UUID lobbyUUID) {
        this.lobbyUUID = lobbyUUID;
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent, should be a {@link IServerPacket}
     */
    synchronized void send(Object message) {
        writeThread.send(message);
    }

    /**
     * Closes this socket connection.
     */
    public synchronized void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    /**
     * Stops this instance and deregister it from the LobbyController.
     */
    private void close() {
        closeConnection();

        System.out.println("Deregistering client...");
        remoteView.getLobbyController().deregisterConnection(this);
    }

    /**
     * Runs this instance, waiting for client messages and handling them.
     */
    @Override
    public void run() {
        ObjectInputStream in;
        try{
            writeThread = new WriteThread(this, socket);
            writeThread.start();
            in = new ObjectInputStream(socket.getInputStream());

            remoteView.getLobbyController().addToLobby(this);

            Object read;
            while(isActive()){
                read = in.readObject();

                remoteView.handlePacket(read);
            }

            writeThread.join();
        } catch (IOException | NoSuchElementException | ClassNotFoundException e) {
            System.err.println("Error!" + e.getMessage());
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}

/**
 * Thread that queues and sends server messages to the client.
 */
class WriteThread extends Thread {
    private static final int BUFFER_CAPACITY = 20;

    private final SocketClientConnection clientConnection;
    private final ObjectOutputStream out;
    private final ArrayBlockingQueue<Object> bufferOut;

    /**
     * Constructs a new WriteThread owned by the given SocketClientConnection, and that writes to the given Socket.
     *
     * @param clientConnection the parent socket client connection
     * @param socket the socket to write to
     * @throws IOException if the ObjectOutputStream can't be created
     */
    WriteThread(SocketClientConnection clientConnection, Socket socket) throws IOException {
        super();

        this.clientConnection = clientConnection;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        bufferOut = new ArrayBlockingQueue<>(BUFFER_CAPACITY);
    }

    /**
     * Starts the write thread loop, waiting for objects to be added to the queue and sending them to the client.
     */
    @Override
    public void run() {
        try {
            while (clientConnection.isActive()) {
                Object object = bufferOut.take();

                out.reset();
                out.writeObject(object);
                out.flush();
            }
        } catch(Exception e) {
            System.err.println("Error in SocketClientConnection WriteThread");
            clientConnection.setInactive();
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent, should be a {@link IServerPacket}
     */
    public synchronized void send(Object message) {
        if(bufferOut.remainingCapacity() > 0) {
            bufferOut.add(message);
        } else {
            System.err.println("WRITE_THREAD: Trying to send too many messages at once!");
        }
    }
}