package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.event.PlayerActionEvent;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.server.event.ServerMessage;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

public class SocketClientConnection extends Observable<Object> implements Runnable {

    private final Socket socket;
    private ObjectOutputStream out;
    private final Server server;
    private final UUID lobbyID;
    private String playerName;
    private RemoteView remoteView;

    private boolean active = true;

    private boolean hasSetPlayersToStart = false;

    public SocketClientConnection(Socket socket, Server server, UUID lobbyID) {
        this.socket = socket;
        this.server = server;
        this.lobbyID = lobbyID;
        this.playerName = null;
        this.remoteView = null;
    }

    private synchronized boolean isActive(){
        return active;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public RemoteView getRemoteView() {
        return remoteView;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayersToStart(int playersNum) {
        server.setPlayersToStart(playersNum);
        hasSetPlayersToStart = true;
    }

    public void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
    }

    synchronized void sendServerMessage(String message) {
        ServerMessage serverMessage = new ServerMessage(message);
        send(serverMessage);
    }

    public void asyncSendServerMessage(final String message){
        new Thread(() -> sendServerMessage(message)).start();
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
        sendServerMessage(ServerMessages.DISCONNECT);
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
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    public void asyncSend(final Object message){
        new Thread(() -> send(message)).start();
    }

    public void addToLobby() {
        server.lobby(this);
    }

    @Override
    public void run() {
        ObjectInputStream in;
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Object read;
            while (playerName == null) {
                sendServerMessage(ServerMessages.INPUT_NAME);
                read = in.readObject();


                System.out.println(read);


                notify(read);
            }

            while (!server.isPlayersToStartSet() && !hasSetPlayersToStart) {
                sendServerMessage(ServerMessages.CHOOSE_PLAYER_NUM);
                read = in.readObject();


                System.out.println(read);


                if(server.isPlayersToStartSet()) {
                    sendServerMessage(ServerMessages.ALREADY_SELECTED);
                    break;
                }
                notify(read);
            }

            while(isActive()){
                read = in.readObject();

                notify(read);
            }
        } catch (IOException | NoSuchElementException | ClassNotFoundException e) {
            System.err.println("Error!" + e.getMessage());
        } finally {
            close();
        }
    }
}
