package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

/**
 * Hello world!
 */
public class App extends Application {
    private static boolean startCli = false, noServer = false, startEditor = false;

    public static void main(String[] args) {
        for(String arg : args) {
            handleCommand(arg.substring(1));
        }
        launch();
    }

    private static void handleCommand(String command) {
        switch (command.toLowerCase()) {
            case "cli" -> startCli = true;
            case "noserver" -> noServer = true;
            case "editor" -> startEditor = true;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        if(startEditor) {
            GameConfigEditor editor = new GameConfigEditor();
            editor.start(stage);
            return;
        }

        Client client;
        try {
            client = new Client(startCli, noServer);
            client.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
    }
}
