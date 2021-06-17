package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.beans.MockModel;
import it.polimi.ingsw.view.beans.MockPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.Map;

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


    public View(Client client) {
        this.client = client;
        this.gameState = new SimpleObjectProperty<>(GameState.CONNECTING);
        this.ownTurn = new SimpleBooleanProperty(false);
        this.usingProductions = false;
        this.model = new MockModel();
    }

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
    public void addToLobby(boolean isFirstConnection) {
        setGameState(GameState.CHOOSING_NAME);
        if (isFirstConnection)
            lobbyMaster = true;
    }

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

    public abstract void handlePlayerDisconnect(String playerName);

    public void handleGameStart(GameConfig gameConfig) {
        getModel().setGameConfig(gameConfig);

        setGameState(GameState.STARTING);
        getRenderer().showLobbyMessage(ViewString.GAME_STARTING);
    }

    public abstract void handlePlayerCrash(String playerName);

    public void handleInvalidAction(String errorMessage) {
        getRenderer().showErrorMessage(errorMessage);
        if (isUsingProductions() && errorMessage.startsWith("Error during production")) {
            getModel().getLocalPlayer().getDeposit().restoreSavedState();
            setUsingProductions(false);
        }
    }

    public abstract void handleEndGame(Map<String, Integer> scores, String winnerName);

    public abstract void handleEndSingleplayerGame(boolean lorenzoWin, String loseReason, int playerScore);

    protected void reset() {
        this.gameState = new SimpleObjectProperty<>(GameState.CONNECTING);
        this.ownTurn = new SimpleBooleanProperty(false);
        this.usingProductions = false;
        this.model = new MockModel();

        getClient().reset();
    }
}

