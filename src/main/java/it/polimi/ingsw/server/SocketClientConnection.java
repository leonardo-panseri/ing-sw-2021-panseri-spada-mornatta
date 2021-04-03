package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

public class SocketClientConnection extends Observable<String> implements ClientConnection, Runnable {

    private final Socket socket;
    private ObjectOutputStream out;
    private final Server server;
    private final UUID lobbyID;
    private String playerName;
    private RemoteView remoteView;

    private boolean active = true;

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
    }

    public void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
    }

    synchronized void sendServerMessage(String message) {
        String jsonMessage = "{\"type\":\"ServerMessages\",\"content\":\"" + message + "\"}";
        send(jsonMessage);
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

    @Override
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

    @Override
    public void asyncSend(final Object message){
        new Thread(() -> send(message)).start();
    }

    public void addToLobby() {
        server.lobby(this);
    }

    @Override
    public void run() {
        Scanner in;
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            String read;
            while (playerName == null) {
                sendServerMessage(ServerMessages.INPUT_NAME);
                read = in.nextLine();


                System.out.println(read);


                notify(read);
            }

            while (server.isLobbyEmpty()) {
                sendServerMessage(ServerMessages.CHOOSE_PLAYER_NUM);
                read = in.nextLine();


                System.out.println(read);


                if(!server.isLobbyEmpty()) {
                    sendServerMessage(ServerMessages.ALREADY_SELECTED);
                    break;
                }
                notify(read);
            }

            /*playerName = read;

            if(server.isLobbyEmpty()) {
                int playerNum = -1;
                while(playerNum == -1) {
                    if(!server.isLobbyEmpty()) {
                        sendServerMessage(ServerMessages.ALREADY_SELECTED);
                        break;
                    }

                    sendServerMessage(ServerMessages.CHOOSE_PLAYER_NUM);
                    read = in.nextLine();
                    try {
                        playerNum = Integer.parseInt(read);
                    } catch (NumberFormatException e) {
                        sendServerMessage(ServerMessages.INVALID_INPUT);
                        playerNum = -1;
                    }
                    if(playerNum > 4 || playerNum <= 0) {
                        sendServerMessage(ServerMessages.INVALID_INPUT);
                        playerNum = -1;
                    }
                }

                if(server.isLobbyEmpty()) {
                    server.setPlayersToStart(playerNum);
                } else {
                    sendServerMessage(ServerMessages.ALREADY_SELECTED);
                }
            }

            server.lobby(this);*/

            while(isActive()){

                System.out.println("Waiting for player input: " + getPlayerName());

                read = in.nextLine();


                System.out.println(read);


                notify(read);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        } finally {
            System.out.println("Sto per chiudere");
            close();
        }
    }
}
