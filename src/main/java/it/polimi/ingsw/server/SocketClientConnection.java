package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.messages.DirectServerMessage;
import it.polimi.ingsw.server.messages.PlayerCrashMessage;
import it.polimi.ingsw.server.messages.ServerMessage;
import it.polimi.ingsw.server.messages.ServerMessages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;

public class SocketClientConnection extends Observable<Object> implements Runnable {
    private final Socket socket;
    private ObjectOutputStream out;

    private final LobbyController lobbyController;
    private String playerName;
    private final RemoteView remoteView;
    private UUID lobbyUUID;

    private boolean active = true;

    public SocketClientConnection(Socket socket, LobbyController lobbyController) {
        this.socket = socket;
        this.lobbyController = lobbyController;
        this.playerName = null;
        this.remoteView = new RemoteView(this);
        this.lobbyUUID = null;
    }

    private synchronized boolean isActive(){
        return active;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public RemoteView getRemoteView() {
        return remoteView;
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    public UUID getLobbyUUID() {
        return lobbyUUID;
    }

    public void setLobbyUUID(UUID lobbyUUID) {
        this.lobbyUUID = lobbyUUID;
    }

    synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
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
        lobbyController.deregisterConnection(this);
        System.out.println("Done!");
    }

    public void asyncSend(final Object message){
        new Thread(() -> send(message)).start();
    }

    @Override
    public void run() {
        ObjectInputStream in;
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            lobbyController.addToLobby(this);

            Object read;
            while(isActive()){
                read = in.readObject();

                notify(read);
            }
        } catch (IOException | NoSuchElementException | ClassNotFoundException e) {
            System.err.println("Error!" + e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}
