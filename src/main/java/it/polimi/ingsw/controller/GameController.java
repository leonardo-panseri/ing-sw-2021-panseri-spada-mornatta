package it.polimi.ingsw.controller;

import it.polimi.ingsw.view.messages.PlayerActionEvent;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;

public class GameController implements Observer<PlayerActionEvent> {
    private final Game game;
    private final TurnController turnController;
    private final PlayerController playerController;

    public GameController() {
        game = new Game();
        turnController = new TurnController(this);
        playerController = new PlayerController(this);
    }

    public synchronized Game getGame() {
        return game;
    }

    public synchronized TurnController getTurnController() {
        return turnController;
    }

    public synchronized PlayerController getPlayerController() {
        return playerController;
    }

    public void addPlayer(Player player) {
        game.addPlayer(player);
    }

    void endGame() {

    }

    int calculateScore(Player player) {
        return 0;
    }

    void exit() {

    }

    void exitGame() {

    }

    @Override
    public synchronized void update(PlayerActionEvent event) {
        event.process(this);
    }

    boolean isPlaying(Player player) {
        return getGame().getCurrentPlayer().equals(player);
    }
}
