package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launcher class for the game client.
 */
public class App extends Application {
    private static boolean startCli = false, noServer = false, startEditor = false;

    /**
     * Launches the game client.
     *
     * @param args an array of arguments, possible values: <ul>
     *             <li>-cli : launches the game in command line mode, without a gui</li>
     *             <li>-noserver : launches the game without connecting to a remote game server</li>
     *             <li>-editor : launches the editor for the game parameters</li>
     * </ul>
     */
    public static void main(String[] args) {
        for (String arg : args) {
            handleCommand(arg.substring(1));
        }
        launch();
    }

    /**
     * Parses and recognizes the given command line argument.
     *
     * @param command one of the following: <ul>
     *             <li>-cli : launches the game in command line mode, without a gui</li>
     *             <li>-noserver : launches the game without connecting to a remote game server</li>
     *             <li>-editor : launches the editor for the game parameters</li>
     * </ul>
     */
    private static void handleCommand(String command) {
        switch (command.toLowerCase()) {
            case "cli" -> startCli = true;
            case "noserver" -> noServer = true;
            case "editor" -> startEditor = true;
        }
    }

    /**
     * Entry method for the JavaFX application.
     *
     * @param stage the main stage of the application
     */
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
