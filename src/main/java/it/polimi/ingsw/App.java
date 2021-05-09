package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;

import java.io.IOException;
import java.util.Locale;

/**
 * Hello world!
 */
public class App {
    private static boolean startCli = false, noServer = false;

    public static void main(String[] args) {
        for(String arg : args) {
            handleCommand(arg.substring(1));
        }

        Client client;
        try {
            client = new Client(startCli, noServer);
            client.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
    }

    private static void handleCommand(String command) {
        switch (command.toLowerCase()) {
            case "cli" -> startCli = true;
            case "noserver" -> noServer = true;
        }
    }
}
