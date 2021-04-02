package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.event.PlayerActionEvent;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

public class SocketClientWrite extends Thread {
    private static final int BUFFER_CAPACITY = 20;

    private final Client client;
    private final PrintWriter socketOut;
    private final ArrayBlockingQueue<String> bufferOut;

    public SocketClientWrite(Client client, PrintWriter socketOut) {
        super();

        this.client = client;
        this.socketOut = socketOut;
        bufferOut = new ArrayBlockingQueue<>(BUFFER_CAPACITY);
    }

    @Override
    public void run() {
        try {
            Iterator<String> messages = bufferOut.iterator();
            while (client.isActive()) {
                while (messages.hasNext()) {
                    String message = messages.next();

                    socketOut.println(message);
                    socketOut.flush();
                }
            }
        }catch(Exception e){
            client.setActive(false);
        }
    }

    public synchronized void send(PlayerActionEvent message) {
        if(bufferOut.remainingCapacity() > 0) {
            bufferOut.add(message.serialize());
        } else {
            System.err.println("WRITE_THREAD: Trying to send too many messages at once!");
        }
    }

}
