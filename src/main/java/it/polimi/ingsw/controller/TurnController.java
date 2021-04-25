package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class TurnController {
    private final GameController gameController;

    public TurnController(GameController gameController) {
        this.gameController = gameController;
    }

    public synchronized void start() {
        gameController.getGame().randomSortPlayers();
        gameController.getGame().setCurrentPlayer(gameController.getGame().getPlayerAt(0));
        for (int i = 0; i < gameController.getGame().getPlayerNum(); i++) {
            List<LeaderCard> draw = gameController.getGame().getDeck().initialDrawLeaders();
            gameController.getGame().getPlayerAt(i).setLeaderCards(draw);
        }
        gameController.getGame().setGamePhase(GamePhase.SELECTING_LEADERS);
    }

    public synchronized void endTurn(Player player) {
        if(!gameController.isPlaying(player)) {
            return;
        }
        int discardedMarketResults = player.getBoard().getDeposit().getUnusedMarketResults();
        if(discardedMarketResults > 0) {
            for(Player p : gameController.getGame().getPlayers())
                if(!p.equals(player)) {
                    p.addFaithPoints(discardedMarketResults);
                    gameController.getPlayerController().checkFaithPointsToWin(p);
                }
        }
        player.getBoard().getDeposit().clearMarketResults();

        if(gameController.getGame().isLastRound() && gameController.getGame().isLastPlayerTurn()) {
            gameController.endGame();
        } else {
            gameController.getGame().nextPlayer();
        }
    }
}
