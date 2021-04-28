package it.polimi.ingsw.client;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.implementation.cli.CLI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Main client instance.
 */
public class Client {
    private final String ip;
    private final int port;
    private SocketClientWrite writeThread;

    private View view;

    private boolean active = true;

    /**
     * Constructs a new Client that will connect to the given ip and port.
     *
     * @param ip the ip of the server
     * @param port the port of the server
     */
    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    /**
     * Checks if this client is still active.
     *
     * @return true if the client is active, false otherwise
     */
    public synchronized boolean isActive(){
        return active;
    }

    /**
     * Sets this client active status.
     *
     * @param active the active status
     */
    public synchronized void setActive(boolean active){
        this.active = active;
    }

    /**
     * Gets the View associated with this Client.
     *
     * @return the view that's associated with this client
     */
    public View getView() {
        return view;
    }

    /**
     * Sends a message to the Server.
     *
     * @param message the message that will be sent to the server
     */
    public void send(Object message) {
        writeThread.send(message);
    }

    /**
     * Starts the main client loop, reading and interpreting user commands.
     *
     * @throws IOException if the creation of the socket input or output stream fails
     */
    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        try{
            Scanner stdin = new Scanner(System.in);
            String input;
            int chosen = -1;
            while (chosen != 1 && chosen != 2){
                System.out.println("Choose interface: \n" +
                        "1) CLI");
                input = stdin.nextLine();
                try{
                    chosen = Integer.parseInt(input);
                } catch (NumberFormatException e){
                    System.out.println("Please input 1 or 2");
                }
            }

            if(chosen == 1) {
                view = new CLI(this);
            } else view = new CLI(this);

            SocketClientRead readThread = new SocketClientRead(this, socketIn);
            writeThread = new SocketClientWrite(this, socketOut);
            readThread.start();
            writeThread.start();

            view.run();

            readThread.join();
            System.out.println("Read thread terminated");
            writeThread.join();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }

}
