package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

public class SocketClientConnection implements Runnable {
    private final Socket socket;
    private WriteThread writeThread;

    private String playerName;
    private final RemoteView remoteView;
    private UUID lobbyUUID;

    private boolean active = true;

    public SocketClientConnection(Socket socket, LobbyController lobbyController) {
        this.socket = socket;
        this.playerName = null;
        this.remoteView = new RemoteView(this, lobbyController);
        this.lobbyUUID = null;
    }

    synchronized boolean isActive(){
        return active;
    }

    synchronized void setActive(boolean active) {
        this.active = active;
    }

    public synchronized String getPlayerName() {
        return playerName;
    }

    public synchronized void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public RemoteView getRemoteView() {
        return remoteView;
    }

    public synchronized UUID getLobbyUUID() {
        return lobbyUUID;
    }

    public synchronized void setLobbyUUID(UUID lobbyUUID) {
        this.lobbyUUID = lobbyUUID;
    }

    synchronized void send(Object message) {
        writeThread.send(message);
    }

    public synchronized void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();

        System.out.println("Deregistering client...");
        remoteView.getLobbyController().deregisterConnection(this);
        System.out.println("Done!");
    }

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
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}

class WriteThread extends Thread {
    private static final int BUFFER_CAPACITY = 20;

    private final SocketClientConnection clientConnection;
    private final ObjectOutputStream out;
    private final ArrayBlockingQueue<Object> bufferOut;

    WriteThread(SocketClientConnection clientConnection, Socket socket) throws IOException {
        super();

        this.clientConnection = clientConnection;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        bufferOut = new ArrayBlockingQueue<>(BUFFER_CAPACITY);
    }

    @Override
    public void run() {
        try {
            while (clientConnection.isActive()) {
                while (bufferOut.size() > 0) {
                    Object object = bufferOut.remove();

                    out.reset();
                    out.writeObject(object);
                    out.flush();
                }
            }
        } catch(Exception e) {
            System.err.println("Error in SocketClientConnection WriteThread");
            clientConnection.setActive(false);
        }
    }

    public synchronized void send(Object message) {
        if(bufferOut.remainingCapacity() > 0) {
            bufferOut.add(message);
        } else {
            System.err.println("WRITE_THREAD: Trying to send too many messages at once!");
        }
    }
}