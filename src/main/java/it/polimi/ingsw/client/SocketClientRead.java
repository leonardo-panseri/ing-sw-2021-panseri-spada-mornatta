package it.polimi.ingsw.client;

import it.polimi.ingsw.server.messages.ServerMessage;
import it.polimi.ingsw.model.messages.*;

import java.io.ObjectInputStream;

/**
 * Thread responsible to read messages coming from the server and handling them.
 */
public class SocketClientRead extends Thread {
    private final Client client;
    private final ObjectInputStream socketIn;

    /**
     * Constructs a new SocketClientRead for the given Client that will read messages from the given ObjectInputStream.
     *
     * @param client the client that is associated with this thread
     * @param socketIn the input stream from where messages will be read
     */
    public SocketClientRead(Client client, ObjectInputStream socketIn) {
        super();

        this.client = client;
        this.socketIn = socketIn;
    }

    /**
     * Starts the read thread loop, waiting for objects to be read from the input stream and handling them.
     */
    @Override
    public void run() {
        try {
            while (client.isActive()) {
                Object inputObject = socketIn.readObject();
                if(inputObject instanceof ServerMessage) {
                    ServerMessage message = (ServerMessage) inputObject;

                    try {
                        message.process(client.getView());
                    } catch (Exception e) {
                        System.err.println("Uncaught exception while processing server message");
                        e.printStackTrace();
                    }
                } else if(inputObject instanceof PropertyUpdate){
                    PropertyUpdate update = (PropertyUpdate) inputObject;

                    System.out.println("Received: " + update);

                    try {
                        update.process(client.getView());
                    } catch (Exception e) {
                        System.err.println("Uncaught exception while processing update");
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Received object of unknown type");
                }
            }
        } catch (Exception e){
            client.setActive(false);
            e.printStackTrace();
        }
    }
}
