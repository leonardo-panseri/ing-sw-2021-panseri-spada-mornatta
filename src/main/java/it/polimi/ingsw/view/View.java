package it.polimi.ingsw.view;

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
    private GameState gameState;
    private String playerName;
    private MockModel model;

    public View() {
        this.gameState = GameState.CONNECTING;
    }

    public abstract void updateGamePhase(GamePhase gamePhase);

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public abstract void showServerMessage(String message);

    public abstract void createDevelopmentDeck(List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck);

    public abstract void updateLeaderCards(Map<LeaderCard, Boolean> ownedLeaders);

    public abstract void updateDevelopmentCards(DevelopmentCard card, int slot);

    public abstract void updateTurn(String playerName);

    public abstract void createMarket(List<List<Resource>> market);

    public abstract void buyDevelopmentCard(String[] args);

    public abstract void printMarket();

    public abstract void printOwnLeaders();

    public abstract void printOwnDevelopmentCards();

    public abstract void printDevelopmentDeck();

    public abstract void printDeposit();
}
