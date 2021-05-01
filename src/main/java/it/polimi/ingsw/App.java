package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Client client;
        try {
            client = new Client("localhost", 12345);
            client.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
    }
}
