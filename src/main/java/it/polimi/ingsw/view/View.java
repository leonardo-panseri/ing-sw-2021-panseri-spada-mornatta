package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Resource;

import java.util.List;

public abstract class View implements Runnable {
    private GameState gameState;
    private String playerName;

    public View() {
        this.gameState = GameState.CONNECTING;
    }

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

    public abstract void createMarket(List<List<Resource>> market);

    public abstract void printMarket();
}
