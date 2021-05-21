package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.beans.MockModel;
import it.polimi.ingsw.view.beans.MockPlayer;

import java.util.Map;

public abstract class View {
    private final Client client;

    private ModelUpdateHandler modelUpdateHandler;
    private Renderer renderer;
    private ActionSender actionSender;
    private final MockModel model;

    private GameState gameState;
    private String playerName;
    private boolean lobbyMaster;
    private boolean ownTurn;
    private boolean usingProductions;


    public View(Client client) {
        this.client = client;
        this.gameState = GameState.CONNECTING;
        this.ownTurn = false;
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
        return gameState;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isLobbyMaster() {
        return lobbyMaster;
    }

    public boolean isOwnTurn() {
        return ownTurn;
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
        this.gameState = gameState;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        getClient().send(new PlayerNameMessage(playerName));
    }

    public void setOwnTurn(boolean ownTurn) {
        this.ownTurn = ownTurn;
    }

    public void setUsingProductions(boolean usingProductions) {
        this.usingProductions = usingProductions;
    }

    /*
        Handle ServerMessages
         */
    public void addToLobby(boolean isFirstConnection) {
        setGameState(GameState.CHOOSING_NAME);
        if(isFirstConnection)
            lobbyMaster = true;
    }

    public void handlePlayerConnect(String playerName, int currentPlayers, int playersToStart) {
        if(playerName.equals(getPlayerName())) {
            MockPlayer localPlayer = getModel().addPlayer(getPlayerName(), true);
            getModel().setLocalPlayer(localPlayer);
            if (isLobbyMaster()) {
                setGameState(GameState.CHOOSING_PLAYERS);
            } else
                setGameState(GameState.WAITING_PLAYERS);
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

    public void handlePlayerDisconnect(String playerName) {
        getRenderer().showLobbyMessage(playerName == null ? ViewString.PLAYER_DISCONNECT :
                                              ViewString.PLAYER_DISCONNECT_WITH_NAME.formatted(playerName));
    }

    public void handleGameStart(GameConfig gameConfig) {
        getModel().setGameConfig(gameConfig);

        setGameState(GameState.STARTING);
        getRenderer().showLobbyMessage(ViewString.GAME_STARTING);
    }

    public void handlePlayerCrash(String playerName) {
        getRenderer().showLobbyMessage(playerName == null ? ViewString.PLAYER_CRASH :
                                              ViewString.PLAYER_CRASH_WITH_NAME.formatted(playerName));
        client.terminate();
    }

    public void handleEndGame(Map<String, Integer> scores, String winnerName) {
        getRenderer().printFinalScores(scores, winnerName);
        client.terminate();
    }

    public void handleEndSingleplayerGame(boolean lorenzoWin, String loseReason, int playerScore) {
        getRenderer().printSingleplayerFinalScore(lorenzoWin, loseReason, playerScore);
        client.terminate();
    }
}

