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

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        if(inputObject instanceof String){
                            String in = (String)inputObject;

                            System.out.println("Received: " + in);

                            JsonObject message;
                            String type;
                            JsonElement content;
                            try {
                                message = JsonParser.parseString(in).getAsJsonObject();
                                type = message.get("type").getAsString();
                                content = message.get("content");
                            } catch (JsonParseException | IllegalStateException | NullPointerException | ClassCastException e) {
                                System.err.println("Received malformed JSON object");
                                break;
                            }

                            PropertyUpdate update = null;
                            String serverMessage = null;
                            switch (type) {
                                case "ServerMessages" -> {
                                    try {
                                        serverMessage = content.getAsString();
                                    } catch (ClassCastException e) {
                                        System.err.println("Can't deserialize ServerMessage");
                                    }
                                }
                                case "BoughtCardUpdate" -> update = deserializeUpdate(content, BoughtCardUpdate.class);
                                case "CreateMarketUpdate" -> update = deserializeUpdate(content, CreateMarketUpdate.class);
                                case "DepositStrongBoxUpdate" -> update = deserializeUpdate(content, DepositStrongboxUpdate.class);
                                case "DepositUpdate" -> update = deserializeUpdate(content, DepositUpdate.class);
                                case "DevelopmentDeckUpdate" -> update = deserializeUpdate(content, DevelopmentDeckUpdate.class);
                                case "FaithUpdate" -> update = deserializeUpdate(content, FaithUpdate.class);
                                case "LorenzoUpdate" -> update = deserializeUpdate(content, LorenzoUpdate.class);
                                case "MarketUpdate" -> update = deserializeUpdate(content, MarketUpdate.class);
                                case "OwnedLeadersUpdate" -> update = deserializeUpdate(content, OwnedLeadersUpdate.class);
                                case "TurnUpdate" -> update = deserializeUpdate(content, TurnUpdate.class);
                                default -> System.err.println("Can't deserialize content");
                            }

                            if(update != null) {
                                System.out.println("Deserialized object of type: " + update.getClass().getSimpleName());
                                System.out.println("Object toString: " + update.toString());
                            }
                            if(serverMessage != null) {
                                System.out.println("Received server message: " + serverMessage);
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (Exception e){
                    setActive(false);
                }
            }

            private PropertyUpdate deserializeUpdate(JsonElement content, Class<?> type) {
                try {
                    return (PropertyUpdate) GsonParser.instance().getGson().fromJson(content, type);
                } catch (JsonSyntaxException e) {
                    System.err.println("Can't deserialize PropertyUpdate");
                }
                return null;
            }
        });
        t.start();
        return t;
    }

    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        String inputLine = stdin.nextLine();
                        socketOut.println(inputLine);
                        socketOut.flush();
                    }
                }catch(Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);

        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdin, socketOut);
            t0.join();
            t1.join();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }

}
