package it.polimi.ingsw.client;

import it.polimi.ingsw.server.messages.ServerMessage;
import it.polimi.ingsw.model.messages.*;

import java.io.ObjectInputStream;

public class SocketClientRead extends Thread {
    private final Client client;
    private final ObjectInputStream socketIn;

    public SocketClientRead(Client client, ObjectInputStream socketIn) {
        super();

        this.client = client;
        this.socketIn = socketIn;
    }

    @Override
    public void run() {
        try {
            while (client.isActive()) {
                Object inputObject = socketIn.readObject();
                if(inputObject instanceof ServerMessage) {
                    ServerMessage message = (ServerMessage) inputObject;
                    message.process(client.getView());
                } else if(inputObject instanceof PropertyUpdate){
                    PropertyUpdate update = (PropertyUpdate) inputObject;

                    System.out.println("Received: " + update);

                    update.process(client.getView());
                } else {
                    System.err.println("Received object of unknown type");
                }
            }
        } catch (Exception e){
            client.setActive(false);
        }
    }
}
