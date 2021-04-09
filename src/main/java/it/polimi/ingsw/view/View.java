package it.polimi.ingsw.view;

public abstract class View implements Runnable {
    private GameState gameState;

    public View() {
        this.gameState = GameState.CONNECTING;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public abstract void showServerMessage(String message);
}
