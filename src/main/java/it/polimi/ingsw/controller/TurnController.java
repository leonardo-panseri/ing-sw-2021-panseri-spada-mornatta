package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.LeaderCard;

import java.util.List;

public class TurnController {
    private final GameController gameController;

    public TurnController(GameController gameController) {
        this.gameController = gameController;
    }

    public void start() {
        gameController.getGame().randomSortPlayers();
        for (int i = 0; i < gameController.getGame().getPlayerNum(); i++) {
            List<LeaderCard> draw = gameController.getGame().getDeck().initialDrawLeaders();
            gameController.getGame().getPlayerAt(i).setLeaderCards(draw);
        }
        gameController.getGame().setCurrentPlayer(gameController.getGame().getPlayerAt(0));
    }

    public void endTurn() {

    }
}
