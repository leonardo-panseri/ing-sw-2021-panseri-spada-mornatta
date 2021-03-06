package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

/**
 * Controller for turn and round management.
 */
public class TurnController {
    private final GameController gameController;

    /**
     * Constructs a new TurnController for the given GameController.
     *
     * @param gameController the game controller that is the parent of this turn controller
     */
    public TurnController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Starts the game, sorting randomly round order and distributing initial LeaderCards to the Players.
     */
    public synchronized void start() {
        gameController.getGame().randomSortPlayers();
        gameController.getGame().setCurrentPlayer(gameController.getGame().getPlayerAt(0));

        for (int i = 0; i < gameController.getGame().getPlayerCount(); i++) {
            List<LeaderCard> draw = gameController.getGame().getDeck().initialDrawLeaders();
            gameController.getGame().getPlayerAt(i).notifyInitialTurn(draw, (i + 1) / 2, (i + 1) / 3);
        }
        gameController.getGame().setGamePhase(GamePhase.SELECTING_LEADERS);
    }

    /**
     * Ends the turn of the given Player. If it's not his turn prints an error message to console and returns. If the
     * Player has any market results that are not stored removes them and award all opponents an amount of faith points
     * equals to the amount of discarded resources. If it's the last player turn in the last round ends the game.
     *
     * @param player the player whose turn will be ended
     */
    public synchronized void endTurn(Player player) {
        gameController.checkTurn(player);

        if (!player.hasAlreadyPlayed()) {
            gameController.getGame().notifyInvalidAction(player, "You must perform at least an action before ending your turn");
            return;
        }

        int discardedMarketResults = player.getBoard().getDeposit().getUnusedMarketResults();
        if (discardedMarketResults > 0) {
            if (gameController.isSinglePlayer()) {
                gameController.getGame().getLorenzo().addPoints(discardedMarketResults);
                gameController.getLorenzoController().checkLorenzoFaith();
            } else {
                for (Player p : gameController.getGame().getPlayers())
                    if (!p.equals(player)) {
                        p.addFaithPoints(discardedMarketResults);
                        gameController.getPlayerController().checkFaithPoints(p);
                    }
            }
            player.getBoard().getDeposit().clearMarketResults();
        }

        player.setHasAlreadyPlayed(false);

        if (!gameController.isSinglePlayer() && gameController.getGame().isLastRound() &&
                gameController.getGame().isLastPlayerTurn()) {
            gameController.endGame();
        } else if (gameController.isSinglePlayer() && gameController.getGame().isLastRound()) {
            gameController.getGame().terminateSingleplayer(false, "",
                    gameController.calculateScore(gameController.getGame().getCurrentPlayer()));
        } else {
            if (gameController.isSinglePlayer())
                gameController.getLorenzoController().executeAction();
            else
                gameController.getGame().nextPlayer();
        }
    }
}
