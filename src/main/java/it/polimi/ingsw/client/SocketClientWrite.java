package it.polimi.ingsw.client;

import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class SocketClientWrite extends Thread {
    private static final int BUFFER_CAPACITY = 20;

    private final Client client;
    private final ObjectOutputStream socketOut;
    private final ArrayBlockingQueue<Object> bufferOut;
    private final Scanner stdin = new Scanner(System.in);

    public SocketClientWrite(Client client, ObjectOutputStream socketOut) {
        super();

        this.client = client;
        this.socketOut = socketOut;
        bufferOut = new ArrayBlockingQueue<>(BUFFER_CAPACITY);
    }

    @Override
    public void run() {
        try {
            while (client.isActive()) {
                while (bufferOut.size() > 0) {
                    Object object = bufferOut.remove();

                    socketOut.reset();
                    socketOut.writeObject(object);
                    socketOut.flush();
                }
            }
        }catch(Exception e){
            client.setActive(false);
        }
    }

    public synchronized void send(Object message) {
        if(bufferOut.remainingCapacity() > 0) {
            bufferOut.add(message);
        } else {
            System.err.println("WRITE_THREAD: Trying to send too many messages at once!");
        }
    }

}
