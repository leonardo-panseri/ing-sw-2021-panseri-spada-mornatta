package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.view.messages.PlayerActionEvent;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;

import java.util.HashMap;
import java.util.Map;

/**
 * Main Game controller holding references to the model and all other controllers.
 */
public class GameController implements Observer<PlayerActionEvent> {
    private final Lobby lobby;
    private final Game game;
    private final TurnController turnController;
    private final PlayerController playerController;

    /**
     * Constructs a new GameController for the given Lobby.
     *
     * @param lobby the lobby that this game controller is part of
     */
    public GameController(Lobby lobby) {
        this.lobby = lobby;
        game = new Game();
        turnController = new TurnController(this);
        playerController = new PlayerController(this);
    }

    /**
     * Gets the current Game model.
     *
     * @return the game model
     */
    public synchronized Game getGame() {
        return game;
    }

    /**
     * Gets the TurnController.
     *
     * @return the turn controller
     */
    public synchronized TurnController getTurnController() {
        return turnController;
    }

    /**
     * Gets the PlayerController.
     *
     * @return the player controller
     */
    public synchronized PlayerController getPlayerController() {
        return playerController;
    }

    /**
     * Adds a new Player to the Game.
     *
     * @param player the player that will be added to the game
     */
    public void addPlayer(Player player) {
        game.addPlayer(player);
    }

    /**
     * Calculates and sends to all players the final scores, than terminate the game.
     */
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
        lobby.terminate();
    }

    /**
     * Gets the total score for the given Player.
     *
     * @param player the player whose score will be calculated
     * @return the score of the player
     */
    private int calculateScore(Player player) {
        int score = 0;

        score += player.getBoard().getDevelopmentCardsTotalVictoryPoints();
        score += game.calculateFaithTrackVictoryPoints(player.getFaithPoints());
        score += player.getPopeFavours();
        score += player.getLeaderCardsTotalVictoryPoints();
        score += Math.floor(player.getBoard().getDeposit().countAllResources() / 5F);

        return score;
    }

    @Override
    public synchronized void update(PlayerActionEvent event) {
        event.process(this);
    }

    /**
     * Checks if it's the turn of the given Player.
     *
     * @param player the player to check
     * @return true if it's the given player's turn, false otherwise
     */
    boolean isPlaying(Player player) {
        return getGame().getCurrentPlayer().equals(player);
    }
}
