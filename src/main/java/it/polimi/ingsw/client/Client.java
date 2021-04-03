package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private String ip;
    private int port;
    private SocketClientRead readThread;
    private SocketClientWrite writeThread;

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

    public void sendMessage(String message) {
        writeThread.sendMessage(message);
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());

        try{
            Scanner stdin = new Scanner(System.in);
            CLI cli;
            int chosen = -1;
            while (chosen != 1 && chosen != 2){
                System.out.println("Choose interface: \n" +
                        "1) CLI");
                chosen = stdin.nextInt();
            }
            if(chosen == 1) {
                cli = new CLI(this);
            } else cli = new CLI(this);
            readThread = new SocketClientRead(this, socketIn, cli);
            writeThread = new SocketClientWrite(this, socketOut, cli);
            readThread.start();
            writeThread.start();
            cli.run();
            readThread.join();
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
