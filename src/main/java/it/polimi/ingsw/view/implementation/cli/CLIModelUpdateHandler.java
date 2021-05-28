package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.constant.AnsiColor;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.ModelUpdateHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.beans.MockPlayer;

import java.util.List;
import java.util.Map;

public class CLIModelUpdateHandler extends ModelUpdateHandler {
    protected CLIModelUpdateHandler(View view) {
        super(view);
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        super.updateGamePhase(gamePhase);
        if(gamePhase == GamePhase.SELECTING_LEADERS && getView().isOwnTurn()) {
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
}
