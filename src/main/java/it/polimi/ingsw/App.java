package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Hello world!
 */
public class App extends Application {
    private static boolean startCli = false, noServer = false, startEditor = false;

    public static void main(String[] args) {
        for (String arg : args) {
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
    public void start(Stage stage) {
        if (startEditor) {
            GameConfigEditor editor = new GameConfigEditor();
            editor.start(stage);
            return;
        }

        Client client;
        client = new Client(stage, startCli, noServer);
        client.run();
    }
}
