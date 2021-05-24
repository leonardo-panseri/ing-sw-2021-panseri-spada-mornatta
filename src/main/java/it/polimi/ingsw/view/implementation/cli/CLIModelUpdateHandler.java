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
        if (playerName.equals(getView().getPlayerName())) {
            getView().getRenderer().showGameMessage(ViewString.OWN_TURN);
            if (getView().getGameState() == GameState.WAIT_SELECT_LEADERS) {
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

    /**
     * Updates a row or column of the market with the resources given.
     *
     * @param index the index of the row or column to update
     * @param changes the updated row or column of the market
     * @param slideResource the resource to be put in the slide
     */
    @Override
    public void updateMarket(int index, List<Resource> changes, Resource slideResource) {
        if (index >= 4) {
            getView().getModel().getMarket().updateMarketRow(index - 4, changes);
        } else getView().getModel().getMarket().updateMarketColumn(index, changes);
        getView().getModel().getMarket().setSlideResource(slideResource);
    }

    /**
     * Updates the deposit of a given player.
     *
     * @param playerName the name of the player whose deposit needs to be updated.
     * @param changes a map representing the new deposit of the player after the updated is made
     * @param leadersDeposit a map representing the new deposit of the leaders of the player after the updated is made
     */

    @Override
    public void updateDeposit(String playerName, Map<Integer, List<Resource>> changes, Map<Integer, List<Resource>> leadersDeposit) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        for (Integer i : changes.keySet()) {
            player.getDeposit().setRow(i - 1, changes.get(i));
        }
        player.getDeposit().setLeadersDeposit(leadersDeposit);
    }

    /**
     * Updates the StrongBox of the given player.
     *
     * @param playerName the name of the player whose strongbox needs to be updated
     * @param strongbox a Map representing the new strongbox of the player after the update is made
     */
    @Override
    public void updateStrongbox(String playerName, Map<Resource, Integer> strongbox) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.getDeposit().setStrongbox(strongbox);
    }

    /**
     * Updates the faithPoints of a given player.
     *
     * @param playerName the name of the player whose faith points need to be updated
     * @param faithPoints an integer representing the new number of faith points after the update is made
     */
    @Override
    public void updateFaith(String playerName, int faithPoints) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.setFaithPoints(faithPoints);
    }

    /**
     * Updates the popeFavours of a given player.
     *
     * @param playerName the name of the player whose pope favours need to be updated
     * @param popeFavours an integer representing the new number of pope favours after the update is made
     */

    @Override
    public void updatePopeFavours(String playerName, int popeFavours) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.setPopeFavours(popeFavours);
    }

    /**
     * Updates the chat.
     *
     * @param sender the name of the sender of the message
     * @param message a string representing the message written by the player
     */
    @Override
    public void updateChat(String sender, String message) {
        getView().getRenderer().printChatMessage(sender, message);
    }

    /**
     * Inserts the resources drawn from the market by a given player.
     *
     * @param playerName the name of the player whose drawn resources need to be stored
     * @param result a list representing the resources to store
     */
    @Override
    public void insertDrawnResources(String playerName, List<Resource> result) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.getDeposit().setMarketResult(result);

        if (player.isLocalPlayer()) {
            getView().getRenderer().printMarketResult();
        }
    }
}
