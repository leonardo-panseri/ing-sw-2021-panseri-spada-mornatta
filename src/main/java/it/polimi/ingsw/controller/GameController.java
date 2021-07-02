package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.view.messages.PlayerActionEvent;

import java.util.*;

/**
 * Main Game controller holding references to the model and all other controllers.
 */
public class GameController implements Observer<PlayerActionEvent> {
    private final Lobby lobby;
    private final Game game;
    private final TurnController turnController;
    private final PlayerController playerController;
    private final LorenzoController lorenzoController;

    /**
     * Constructs a new GameController for the given Lobby.
     *
     * @param lobby the lobby that this game controller is part of
     */
    public GameController(Lobby lobby) {
        this.lobby = lobby;
        GameConfig gameConfig = lobby.getCustomGameConfig();
        if (gameConfig != null)
            game = new Game(gameConfig);
        else
            game = new Game();
        turnController = new TurnController(this);
        playerController = new PlayerController(this);
        if (lobby.isSinglePlayer())
            lorenzoController = new LorenzoController(this);
        else
            lorenzoController = null;
    }

    /**
     * Checks if the Game is single player.
     *
     * @return true if there is only 1 player in the game, false otherwise
     */
    public synchronized boolean isSinglePlayer() {
        return lobby.isSinglePlayer();
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
     * Gets the LorenzoController.
     *
     * @return the lorenzo controller if this game is single player, null otherwise
     */
    public synchronized LorenzoController getLorenzoController() {
        return lorenzoController;
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
        for (Player player : game.getPlayers()) {
            int score = calculateScore(player);
            if (score > maxScore) {
                maxScore = score;
            }
            scores.put(player.getNick(), score);
        }

        int maxResource = 0;
        for (Player player : game.getPlayers()) {
            if (maxScore == calculateScore(player)) {
                int resources = player.getBoard().getDeposit().countAllResources();
                if (resources > maxResource) {
                    maxResource = resources;
                    winner = player.getNick();
                }
            }
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<String, Integer> results = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            results.put(entry.getKey(), entry.getValue());
        }

        game.terminate(results, winner);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                lobby.terminate();
            }
        }, 10000L); //Wait for 10 seconds before closing all connections to give time to all clients to terminate properly
    }

    /**
     * Gets the total score for the given Player.
     *
     * @param player the player whose score will be calculated
     * @return the score of the player
     */
    int calculateScore(Player player) {
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
        try {
            event.process(this);
        } catch (IllegalStateException ignored) {
            //Ignored exception for player action that is not during his turn
        }

    }

    /**
     * Checks if it's the turn of the given Player.
     *
     * @param player the player to check
     * @throws IllegalStateException if it is not the turn of the given player
     */
    synchronized void checkTurn(Player player) throws IllegalStateException {
        if (!getGame().getCurrentPlayer().equals(player)) {
            String errorMessage = "Not your turn!";
            game.notifyInvalidAction(player, errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    /**
     * Checks if the player has already performed one of the three major actions.
     *
     * @param player the player to check
     * @throws IllegalStateException if the player has already performed one of the three major actions
     */
    synchronized void checkAlreadyPlayed(Player player) throws IllegalStateException {
        if (player.hasAlreadyPlayed()) {
            String errorMessage = "You have already done one of the three major actions!";
            game.notifyInvalidAction(player, errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }
}
