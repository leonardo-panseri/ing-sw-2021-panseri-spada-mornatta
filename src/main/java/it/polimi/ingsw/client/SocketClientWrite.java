package it.polimi.ingsw.client;

import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Thread responsible to queue and send messages to the Server.
 */
public class SocketClientWrite extends Thread {
    private static final int BUFFER_CAPACITY = 20;

    private final Client client;
    private final ObjectOutputStream socketOut;
    private final ArrayBlockingQueue<Object> bufferOut;

    /**
     * Constructs a new SocketClientWrite for the given Client that will write to the given ObjectOutputStream.
     *
     * @param client the client that is associated with this thread
     * @param socketOut the output stream where messages will be sent
     */
    public SocketClientWrite(Client client, ObjectOutputStream socketOut) {
        super();

        this.client = client;
        this.socketOut = socketOut;
        bufferOut = new ArrayBlockingQueue<>(BUFFER_CAPACITY);
    }

    /**
     * Starts the write thread loop, waiting for objects to be added to the queue and sending them to the server.
     */
    @Override
    public void run() {
        try {
            while (client.isActive()) {
                Object object = bufferOut.take();

                socketOut.reset();
                socketOut.writeObject(object);
                socketOut.flush();
            }
        }catch(Exception e){
            client.setActive(false);
            e.printStackTrace();
        }
    }

    /**
     * Adds a message to the queue to be sent to the Server. If the queue does not have enough capacity prints an error
     * message in the console.
     *
     * @param message the object that will be sent to the server
     */
    public synchronized void send(Object message) {
        if(bufferOut.remainingCapacity() > 0) {
            bufferOut.add(message);
        } else {
            System.err.println("WRITE_THREAD: Trying to send too many messages at once!");
        }
    }

}
