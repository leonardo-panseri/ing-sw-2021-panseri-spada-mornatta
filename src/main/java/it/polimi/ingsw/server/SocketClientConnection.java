package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;

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

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server, UUID lobbyID) {
        this.socket = socket;
        this.server = server;
        this.lobbyID = lobbyID;
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

    private synchronized void send(Object message) {
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
        send(ServerMessages.DISCONNECT);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    @Override
    public void run() {
        Scanner in;
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send(ServerMessages.INPUT_NAME);
            String read = in.nextLine();
            playerName = read;
            server.lobby(this);
            read = in.nextLine();
            server.ready(this);
            while(isActive()){
                read = in.nextLine();
                notify(read);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        } finally {
            close();
        }
    }
}
