package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.event.PlayerActionEvent;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class SocketClientWrite extends Thread {
    private static final int BUFFER_CAPACITY = 20;

    private final Client client;
    private final PrintWriter socketOut;
    private final ArrayBlockingQueue<String> bufferOut;
    private final Scanner stdin = new Scanner(System.in);
    private final CLI cli;

    public SocketClientWrite(Client client, PrintWriter socketOut, CLI cli) {
        super();

        this.client = client;
        this.socketOut = socketOut;
        this.cli = cli;
        bufferOut = new ArrayBlockingQueue<>(BUFFER_CAPACITY);
    }

    @Override
    public void run() {
        try {
            while (client.isActive()) {
                while (bufferOut.size() > 0) {
                    String message = bufferOut.remove();

                    socketOut.println(message);
                    socketOut.flush();
                }
            }
        }catch(Exception e){
            client.setActive(false);
        }
    }

    public synchronized void sendMessage(String message) {
        if(bufferOut.remainingCapacity() > 0) {
            bufferOut.add(message);
        } else {
            System.err.println("WRITE_THREAD: Trying to send too many messages at once!");
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