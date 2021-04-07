package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.event.PlayerActionEvent;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;

import java.util.List;
import java.util.Map;

public class GameController implements Observer<PlayerActionEvent> {
    private final Game game;
    private final TurnController turnController;
    private final PlayerController playerController;

    public GameController() {
        game = new Game();
        turnController = new TurnController(this);
        playerController = new PlayerController(this);
    }

    public Game getGame() {
        return game;
    }

    public TurnController getTurnController() {
        return turnController;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public void addPlayer(Player player) {
        game.addPlayer(player);
    }

    public int calculateScore(Player player) {
        return 0;
    }

    public void exit() {

    }

    private void exitGame() {

    }

    @Override
    public void update(PlayerActionEvent event) {
        event.process(this);
    }
}
