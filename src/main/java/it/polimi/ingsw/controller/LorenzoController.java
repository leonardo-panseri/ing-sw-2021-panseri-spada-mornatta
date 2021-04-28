package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.lorenzo.action.LorenzoAction;

public class LorenzoController {
    private final GameController gameController;

    public LorenzoController(GameController gameController) {
        this.gameController = gameController;
    }

    public void executeAction() {
        LorenzoAction nextAction = gameController.getGame().getLorenzo().popAction();
        nextAction.execute(this);

        gameController.getGame().getLorenzo().endTurn(gameController.getGame().getCurrentPlayer().getNick());
    }

    public void executeDevelopmentAction(CardColor color) {
        gameController.getGame().getDeck().removeTwoDevelopmentCards(color);
    }

    public void executeMoveAction() {
        gameController.getGame().getLorenzo().addPoints(2);
    }

    public void executeMoveShuffleAction() {
        gameController.getGame().getLorenzo().addPoints(1);
        gameController.getGame().getLorenzo().shuffleActions();
    }
}
