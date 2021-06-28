package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.view.implementation.cli.utils.ViewString;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.beans.MockModel;
import it.polimi.ingsw.view.beans.MockPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.Map;

/**
 * Class responsible of orchestrating all the functionalities of the game interface.
 */
public abstract class View {
    private final Client client;

    private ModelUpdateHandler modelUpdateHandler;
    private Renderer renderer;
    private ActionSender actionSender;
    private MockModel model;

    private ObjectProperty<GameState> gameState;
    private String playerName;
    private boolean lobbyMaster;
    private BooleanProperty ownTurn;
    private boolean usingProductions;

    /**
     * Constructs a new View.
     *
     * @param client the client responsible for this view
     */
    public View(Client client) {
        this.client = client;
        this.gameState = new SimpleObjectProperty<>(GameState.CONNECTING);
        this.ownTurn = new SimpleBooleanProperty(false);
        this.usingProductions = false;
        this.model = new MockModel();
    }

    /**
     * Entry method to initialize the View.
     */
    public abstract void run();

    /**
     * Gets the client.
     *
     * @return the client associated with the view
     */
    public Client getClient() {
        return client;
    }

    public ModelUpdateHandler getModelUpdateHandler() {
        return modelUpdateHandler;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public ActionSender getActionSender() {
        return actionSender;
    }

    public GameState getGameState() {
        return gameState.get();
    }

    public ObjectProperty<GameState> gameStateProperty() {
        return gameState;
    }

    public BooleanProperty ownTurnProperty() {
        return ownTurn;
    }

    public String getPlayerName() {
        return playerName;
    }

    /**
     * Checks if the local player is the first one that joined the lobby.
     *
     * @return true if the player is the first one that joined the lobby, false otherwise
     */
    public boolean isLobbyMaster() {
        return lobbyMaster;
    }

    public boolean isOwnTurn() {
        return ownTurn.get();
    }

    public boolean isUsingProductions() {
        return usingProductions;
    }

    public MockModel getModel() {
        return model;
    }

    public void setModelUpdateHandler(ModelUpdateHandler modelUpdateHandler) {
        this.modelUpdateHandler = modelUpdateHandler;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setActionSender(ActionSender actionSender) {
        this.actionSender = actionSender;
    }

    public void setGameState(GameState gameState) {
        this.gameState.setValue(gameState);
    }

    /**
     * Sets the local player username.
     *
     * @param playerName the local player username
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        getClient().send(new PlayerNameMessage(playerName));
    }

    public void setOwnTurn(boolean ownTurn) {
        this.ownTurn.setValue(ownTurn);
    }

    public void setUsingProductions(boolean usingProductions) {
        if (!this.usingProductions && usingProductions)
            getModel().getLocalPlayer().getDeposit().saveCurrentState();
        this.usingProductions = usingProductions;
    }

    /*
        Handle ServerMessages
         */

    /**
     * Handles the successful connection to a server lobby.
     *
     * @param isFirstConnection true if this client is the first to connect to the lobby, false otherwise
     */
    public void addToLobby(boolean isFirstConnection) {
        setGameState(GameState.CHOOSING_NAME);
        if (isFirstConnection)
            lobbyMaster = true;
    }

    /**
     * Handles the connection of an other player to the lobby.
     *
     * @param playerName the name of the player that connected
     * @param currentPlayers the amount of players connected to the lobby
     * @param playersToStart the number of players required to start the game
     * @param otherConnectedPlayers the list of the other connected player names
     */
    public void handlePlayerConnect(String playerName, int currentPlayers, int playersToStart, List<String> otherConnectedPlayers) {
        if (playerName.equals(getPlayerName())) {
            MockPlayer localPlayer = getModel().addPlayer(getPlayerName(), true);
            getModel().setLocalPlayer(localPlayer);
            if (getClient().isNoServer()) {
                setGameState(GameState.CHOOSING_GAME_CONFIG);
            } else if (isLobbyMaster()) {
                setGameState(GameState.CHOOSING_PLAYERS);
            } else
                setGameState(GameState.WAITING_PLAYERS);

            if (!otherConnectedPlayers.isEmpty()) {
                otherConnectedPlayers.forEach(player -> getModel().addPlayer(player, false));
            }
        } else {
            getModel().addPlayer(playerName, false);
        }
    }

    public void handleSetPlayersToStart(int playersToStart) {
        setGameState(GameState.CHOOSING_GAME_CONFIG);
    }

    public void handleSetGameConfig() {
        setGameState(GameState.WAITING_PLAYERS);
    }

    /**
     * Handles the disconnection of one of the other players from the lobby.
     *
     * @param playerName the name of the player that disconnected
     */
    public abstract void handlePlayerDisconnect(String playerName);

    /**
     * Handles the game start.
     *
     * @param gameConfig the game config used for this game
     */
    public void handleGameStart(GameConfig gameConfig) {
        getModel().setGameConfig(gameConfig);

        setGameState(GameState.STARTING);
        getRenderer().showLobbyMessage(ViewString.GAME_STARTING);
    }

    /**
     * Handles the disconnection of one of the other players, terminating the game.
     *
     * @param playerName the name of the player that disconnected
     */
    public abstract void handlePlayerCrash(String playerName);

    /**
     * Handles an invalid action done by this client.
     *
     * @param errorMessage error message given by the server
     */
    public void handleInvalidAction(String errorMessage) {
        getRenderer().showErrorMessage(errorMessage);
        if (isUsingProductions() && errorMessage.startsWith("Error during production")) {
            getModel().getLocalPlayer().getDeposit().restoreSavedState();
            setUsingProductions(false);
        }
    }

    /**
     * Handles the ending of the game.
     *
     * @param scores a map associating usernames with their scores
     * @param winnerName the username of the winner
     */
    public abstract void handleEndGame(Map<String, Integer> scores, String winnerName);

    /**
     * Handles the ending of the singleplayer game.
     *
     * @param lorenzoWin true if the opponent has won the game, false otherwise
     * @param loseReason the reason of the defeat, may be null
     * @param playerScore the score of the player, valid only if lorenzoWin is false
     */
    public abstract void handleEndSingleplayerGame(boolean lorenzoWin, String loseReason, int playerScore);

    /**
     * Resets the View to the pre game state.
     */
    protected void reset() {
        this.gameState = new SimpleObjectProperty<>(GameState.CONNECTING);
        this.ownTurn = new SimpleBooleanProperty(false);
        this.usingProductions = false;
        this.model = new MockModel();

        getClient().reset();
    }
}

