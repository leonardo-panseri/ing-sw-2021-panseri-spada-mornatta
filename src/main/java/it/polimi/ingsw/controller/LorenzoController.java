package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.lorenzo.action.LorenzoAction;

/**
 * Controller for the single player opponent.
 */
public class LorenzoController {
    private final GameController gameController;

    /**
     * Constructs a new LorenzoController for the given GameController.
     *
     * @param gameController the game controller that is the parent of this lorenzo controller
     */
    public LorenzoController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Invoke the method to execute the next Lorenzo action, then ends its turn.
     */
    public void executeAction() {
        LorenzoAction nextAction = gameController.getGame().getLorenzo().popAction();
        boolean isGameOver = nextAction.execute(this);
        if(!isGameOver)
            gameController.getGame().getLorenzo().endTurn(gameController.getGame().getCurrentPlayer().getNick(), nextAction);
    }

    /**
     * Discards 2 DevelopmentCards from the Deck.
     *
     * @param color the color of the cards to discard
     * @return true if, after this action, the game is over, false otherwise
     */
    public boolean executeDevelopmentAction(CardColor color) {
        gameController.getGame().getDeck().removeTwoDevelopmentCards(color);
        if(gameController.getGame().getDeck().isColorEmpty(color)) {
            gameController.getGame().terminateSingleplayer(true,
                    "Lorenzo has destroyed all " + color.toString().toLowerCase() + " development cards", -1);
            return true;
        }
        return false;
    }

    /**
     * Adds 2 faith points to Lorenzo.
     *
     * @return true if, after this action, the game is over, false otherwise
     */
    public boolean executeMoveAction() {
        gameController.getGame().getLorenzo().addPoints(2);
        return checkLorenzoFaith();
    }

    /**
     * Adds 1 faith point to Lorenzo, then shuffle its actions.
     *
     * @return true if, after this action, the game is over, false otherwise
     */
    public boolean executeMoveShuffleAction() {
        gameController.getGame().getLorenzo().addPoints(1);
        gameController.getGame().getLorenzo().shuffleActions();
        return checkLorenzoFaith();
    }

    /**
     * Checks if Lorenzo has won the match by having more than 23 faith points and if so terminates the game.
     * Also checks for activation of a pope report.
     *
     * @return true if the game is over, false otherwise
     */
    private boolean checkLorenzoFaith() {
        int popeReportSlot = gameController.getGame().checkForPopeReportSlot(gameController.getGame().getLorenzo().getFaithPoints());
        if(popeReportSlot != -1)
            gameController.getGame().activatePopeReport(popeReportSlot);
        if(gameController.getGame().getLorenzo().getFaithPoints() > 23) {
            gameController.getGame().terminateSingleplayer(true,
                    "Lorenzo has reached the end of the faith track", -1);
            return true;
        }
        return false;
    }
}
