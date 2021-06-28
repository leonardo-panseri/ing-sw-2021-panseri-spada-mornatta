package it.polimi.ingsw;

/**
 * Helper class to launch the game client, needed for jar packaging with maven shade (the launcher class for a JavaFX
 * project should not extend Application).
 */
public class AppLauncher {
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
        App.main(args);
    }
}
