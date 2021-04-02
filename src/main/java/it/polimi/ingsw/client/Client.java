package it.polimi.ingsw.client;

import com.google.gson.*;
import it.polimi.ingsw.constant.GsonParser;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.event.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private String ip;
    private int port;

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

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());

        try{
            SocketClientRead readThread = new SocketClientRead(this, socketIn);
            SocketClientWrite writeThread = new SocketClientWrite(this, socketOut);
            readThread.start();
            writeThread.start();
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
