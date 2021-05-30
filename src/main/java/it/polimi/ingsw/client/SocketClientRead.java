package it.polimi.ingsw.client;

import it.polimi.ingsw.IProcessablePacket;
import it.polimi.ingsw.server.IServerPacket;
import java.io.ObjectInputStream;
import java.net.SocketException;

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
                Object packet = socketIn.readObject();

                if(packet instanceof IProcessablePacket) {
                    if(packet instanceof IServerPacket) {
                        IServerPacket serverPacket = (IServerPacket) packet;

                        System.out.println("Received: " + packet);

                        try {
                            serverPacket.process(client.getView());
                        } catch (Exception e) {
                            System.err.println("Uncaught exception while processing server packet");
                            e.printStackTrace();
                        }
                    } else {
                        System.err.println("Received a packet of the wrong type");
                    }
                } else {
                    System.err.println("Received object of unknown type");
                }
            }
        } catch (SocketException ignored) {

        } catch (Exception e){
            e.printStackTrace();
            client.terminate();
        }
    }
}
