package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.view.messages.PlayerActionEvent;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

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
        Map<String, Integer> scores = new HashMap<>();
        int maxScore = 0;
        String winner = "";
        for(Player player : game.getPlayers()) {
            int score = calculateScore(player);
            if(score > maxScore) {
                maxScore = score;
                winner = player.getNick();
            }
            scores.put(player.getNick(), score);
        }
        game.terminate(scores, winner);
    }

    int calculateScore(Player player) {
        int score = 0;

        score += player.getBoard().getDevelopmentCardsTotalVictoryPoints();
        score += game.calculateFaithTrackVictoryPoints(player.getFaithPoints());
        score += player.getPopeFavours();
        score += player.getLeaderCardsTotalVictoryPoints();
        score += Math.floor(player.getBoard().getDeposit().countAllResources() / 5F);

        return score;
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
