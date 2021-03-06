package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.view.implementation.cli.utils.AnsiColor;
import it.polimi.ingsw.view.implementation.cli.utils.ViewString;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.lorenzo.action.LorenzoAction;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.ModelUpdateHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.beans.MockPlayer;

import java.util.List;

/**
 * Implementation of ModelUpdateHandler for the CLI.
 */

public class CLIModelUpdateHandler extends ModelUpdateHandler {

    /**
     * Creates a new CLIModelUpdateHandler for the given View.
     * @param view the cli to be associated with this updater
     */
    protected CLIModelUpdateHandler(View view) {
        super(view);
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        super.updateGamePhase(gamePhase);
        if (gamePhase == GamePhase.SELECTING_LEADERS && getView().isOwnTurn()) {
            getView().getRenderer().showGameMessage(ViewString.SELECT_LEADERS);
            getView().getRenderer().printOwnLeaders();
        }
    }

    @Override
    public void updateTurn(String playerName) {
        super.updateTurn(playerName);
        if (playerName.equals(getView().getPlayerName())) {
            getView().getRenderer().showGameMessage(ViewString.OWN_TURN);
            if (getView().getGameState() == GameState.SELECT_LEADERS) {
                getView().getRenderer().showGameMessage(ViewString.SELECT_LEADERS);
                getView().getRenderer().printOwnLeaders();
            } else if (getView().getGameState() == GameState.PLAYING) {
                getView().getRenderer().showGameMessage(ViewString.CHOOSE_ACTION);
            }
        } else {
            String italicizedPlayerName = AnsiColor.italicize(playerName) + AnsiColor.BLUE;
            getView().getRenderer().showGameMessage(ViewString.OTHER_TURN.formatted(italicizedPlayerName));
        }
    }

    @Override
    public void insertDrawnResources(String playerName, List<Resource> result) {
        super.insertDrawnResources(playerName, result);

        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player.isLocalPlayer()) {
            getView().getRenderer().printMarketResult();
        }
    }

    @Override
    public void updateChat(String sender, String message) {
        getView().getRenderer().printChatMessage(sender, message);
    }

    @Override
    public void handleLorenzoAction(LorenzoAction action) {
        super.handleLorenzoAction(action);
        getView().getRenderer().showGameMessage(action.toString());
    }
}
