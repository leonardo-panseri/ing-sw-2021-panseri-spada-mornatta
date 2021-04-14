package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public abstract class View implements Runnable {
    private final Client client;

    private GameState gameState;
    private String playerName;
    private boolean lobbyMaster;
    private MockModel model;

    public View(Client client) {
        this.client = client;
        this.gameState = GameState.CONNECTING;
    }

    /*
    Show Messages
     */
    public abstract void showDirectMessage(String message);

    public abstract void showLobbyMessage(String message);

    public abstract void showErrorMessage(String message);

    /*
    Handle ServerMessages
     */
    public void addToLobby(boolean isFirstConnection) {
        setGameState(GameState.CHOOSING_NAME);
        if(isFirstConnection)
            lobbyMaster = true;
        showDirectMessage(ViewString.CHOOSE_NAME);
    }

    public void handlePlayerConnect(String playerName, int currentPlayers, int playersToStart) {
        boolean playersToStartSet = playersToStart != -1;
        showLobbyMessage(playersToStartSet ? ViewString.PLAYER_CONNECTED_WITH_COUNT.formatted(playerName, currentPlayers, playersToStart) :
                ViewString.PLAYER_CONNECTED.formatted(playerName));

        if(playerName.equals(getPlayerName()))
            if(isLobbyMaster()) {
                setGameState(GameState.CHOOSING_PLAYERS);
                showDirectMessage(ViewString.CHOOSE_PLAYERS_TO_START);
            } else
                setGameState(GameState.WAITING_PLAYERS);
    }

    public void handleSetPlayersToStart() {
        setGameState(GameState.WAITING_PLAYERS);
        showDirectMessage(ViewString.PLAYERS_TO_START_SET);
    }

    public void handlePlayerDisconnect(String playerName) {
        showLobbyMessage(playerName == null ? ViewString.PLAYER_DISCONNECT :
                                              ViewString.PLAYER_DISCONNECT_WITH_NAME.formatted(playerName));
    }

    public void handleGameStart() {
        setGameState(GameState.STARTING);
        showLobbyMessage(ViewString.GAME_STARTING);
    }

    public void handlePlayerCrash(String playerName) {
        showLobbyMessage(playerName == null ? ViewString.PLAYER_CRASH :
                                              ViewString.PLAYER_CRASH_WITH_NAME.formatted(playerName));
        client.setActive(false);
    }

    /*
    ...
     */
    public abstract void updateGamePhase(GamePhase gamePhase);

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Client getClient() {
        return client;
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

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public abstract void createDevelopmentDeck(List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck);

    public abstract void updateLeaderCards(Map<LeaderCard, Boolean> ownedLeaders);

    public abstract void updateDevelopmentCards(DevelopmentCard card, int slot);

    public abstract void updateTurn(String playerName);

    public abstract void createMarket(List<List<Resource>> market);

    public abstract void updateMarket(int index, List<Resource> changes);

    public abstract void buyDevelopmentCard(int cardIndex);

    public abstract void draw(int marketIndex, Resource whiteConversion);

    public abstract void insertDrawnResources();

    public abstract void printMarket();

    public abstract void printOwnLeaders();

    public abstract void printOwnDevelopmentCards();

    public abstract void printDevelopmentDeck();

    public abstract void printDeposit();

    public abstract void renderCard(DevelopmentCard card);
}
