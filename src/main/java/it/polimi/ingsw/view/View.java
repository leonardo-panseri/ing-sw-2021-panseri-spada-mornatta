package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.constant.ViewString;

public abstract class View implements Runnable {
    private final Client client;

    private ModelUpdateHandler modelUpdateHandler;
    private Renderer renderer;
    private ActionSender actionSender;
    private final MockModel model;

    private GameState gameState;
    private String playerName;
    private boolean lobbyMaster;
    private boolean ownTurn;
    private boolean alreadyPlayed;


    public View(Client client) {
        this.client = client;
        this.gameState = GameState.CONNECTING;
        this.ownTurn = false;
        this.alreadyPlayed = false;
        this.model = new MockModel();
    }

    /*
    Getters & Setters
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

    public boolean hasAlreadyPlayed() {
        return alreadyPlayed;
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
    }

    public void setOwnTurn(boolean ownTurn) {
        this.ownTurn = ownTurn;
    }

    public void setAlreadyPlayed(boolean alreadyPlayed) {
        this.alreadyPlayed = alreadyPlayed;
    }

    /*
    Handle ServerMessages
     */
    public void addToLobby(boolean isFirstConnection) {
        setGameState(GameState.CHOOSING_NAME);
        if(isFirstConnection)
            lobbyMaster = true;
        getRenderer().showGameMessage(ViewString.CHOOSE_NAME);
    }

    public void handlePlayerConnect(String playerName, int currentPlayers, int playersToStart) {
        boolean playersToStartSet = playersToStart != -1;
        getRenderer().showLobbyMessage(playersToStartSet ? ViewString.PLAYER_CONNECTED_WITH_COUNT.formatted(playerName, currentPlayers, playersToStart) :
                ViewString.PLAYER_CONNECTED.formatted(playerName));

        if(playerName.equals(getPlayerName()))
            if(isLobbyMaster()) {
                setGameState(GameState.CHOOSING_PLAYERS);
                getRenderer().showGameMessage(ViewString.CHOOSE_PLAYERS_TO_START);
            } else
                setGameState(GameState.WAITING_PLAYERS);
    }

    public void handleSetPlayersToStart() {
        setGameState(GameState.WAITING_PLAYERS);
        getRenderer().showGameMessage(ViewString.PLAYERS_TO_START_SET);
    }

    public void handlePlayerDisconnect(String playerName) {
        getRenderer().showLobbyMessage(playerName == null ? ViewString.PLAYER_DISCONNECT :
                                              ViewString.PLAYER_DISCONNECT_WITH_NAME.formatted(playerName));
    }

    public void handleGameStart() {
        setGameState(GameState.STARTING);
        getRenderer().showLobbyMessage(ViewString.GAME_STARTING);
    }

    public void handlePlayerCrash(String playerName) {
        getRenderer().showLobbyMessage(playerName == null ? ViewString.PLAYER_CRASH :
                                              ViewString.PLAYER_CRASH_WITH_NAME.formatted(playerName));
        client.setActive(false);
    }
}

