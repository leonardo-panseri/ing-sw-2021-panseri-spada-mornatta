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

    /**
     * Updates the gamePhase. The different states in which the game can be are: {@link GamePhase#SELECTING_LEADERS},
     * {@link GamePhase#PLAYING}, {@link GamePhase#END}.
     *
     * @param gamePhase The different states in which the game can be are: {@link GamePhase#SELECTING_LEADERS},
     *        {@link GamePhase#PLAYING}, {@link GamePhase#END}
     */
    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        switch (gamePhase) {
            case SELECTING_LEADERS -> {
                if (getView().isOwnTurn()) {
                    getView().setGameState(GameState.SELECT_LEADERS);
                    getView().getRenderer().showGameMessage(ViewString.SELECT_LEADERS);
                    getView().getRenderer().printOwnLeaders();
                } else {
                    getView().setGameState(GameState.WAIT_SELECT_LEADERS);
                }
            }
            case PLAYING -> getView().setGameState(GameState.PLAYING);
        }
    }

    /**
     * Updates the LeaderCards of the given player if his GamePhase is {@link GamePhase#PLAYING}.
     *
     * @param playerName the name of the given player
     * @param ownedLeaders a map representing the LeaderCards owned by the given player {@link GamePhase#PLAYING},
     *                     with a boolean attribute that indicates if the LeaderCard is active
     */
    @Override
    public void updateLeaderCards(String playerName, Map<LeaderCard, Boolean> ownedLeaders) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        if (player.isLocalPlayer()) {
            if(getView().getGameState() == GameState.PLAYING) {
                if(ownedLeaders.size() == player.getLeaderCards().size())
                    getView().getRenderer().showGameMessage("Leader card successfully activated");
                else
                    getView().getRenderer().showGameMessage("Leader card discarded (+1 Faith)");
            }
        }

        player.setLeaderCards(ownedLeaders);
    }

    /**
     * Updates the development cards of the given player.
     *
     * @param playerName the name of the given player
     * @param card the development card to set
     * @param slot the slot in which the development card will be set
     */

    @Override
    public void updateDevelopmentCards(String playerName, DevelopmentCard card, int slot) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.getPlayerBoard().setNewDevelopmentCard(card, slot);
    }

    /**
     * Updates the turn of the given player. The states of the game are {@link GameState#CONNECTING},
     * {@link GameState#PLAYING}, {@link GameState#SELECT_LEADERS}, {@link GameState#WAIT_SELECT_LEADERS},
     * {@link GameState#STARTING}, {@link GameState#CHOOSING_NAME}, {@link GameState#CHOOSING_PLAYERS}.
     *
     * @param playerName the name of the player to update the turn
     */
    @Override
    public void updateTurn(String playerName) {
        if (playerName.equals(getView().getPlayerName())) {
            getView().setOwnTurn(true);
            getView().getRenderer().showGameMessage(ViewString.OWN_TURN);
            if (getView().getGameState() == GameState.WAIT_SELECT_LEADERS) {
                getView().setGameState(GameState.SELECT_LEADERS);
                getView().getRenderer().showGameMessage(ViewString.SELECT_LEADERS);
                getView().getRenderer().printOwnLeaders();
            } else if (getView().getGameState() == GameState.PLAYING) {
                getView().getRenderer().showGameMessage(ViewString.CHOOSE_ACTION);
            }
        } else {
            getView().setOwnTurn(false);
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
