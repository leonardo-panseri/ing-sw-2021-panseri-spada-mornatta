package it.polimi.ingsw.client;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.implementation.cli.CLI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private final String ip;
    private final int port;
    private SocketClientWrite writeThread;

    private View view;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    private boolean active = true;

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    public View getView() {
        return view;
    }

    public void send(Object message) {
        writeThread.send(message);
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        try{
            Scanner stdin = new Scanner(System.in);
            int chosen = -1;
            while (chosen != 1 && chosen != 2){
                System.out.println("Choose interface: \n" +
                        "1) CLI");
                chosen = stdin.nextInt();
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
