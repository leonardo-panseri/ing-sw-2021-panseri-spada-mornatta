package it.polimi.ingsw.view;

import it.polimi.ingsw.client.messages.GameConfigMessage;
import it.polimi.ingsw.client.messages.PlayersToStartMessage;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.messages.EndTurnPlayerActionEvent;
import it.polimi.ingsw.view.messages.production.Production;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class ActionSender {
    private final View view;

    private final List<Production> pendingProductions;

    public ActionSender(View view) {
        this.view = view;
        this.pendingProductions = new ArrayList<>();
    }

    public View getView() {
        return view;
    }

    public List<Production> getPendingProductions() {
        return pendingProductions;
    }

    protected void addPendingProduction(Production production) {
        pendingProductions.add(production);
    }

    protected void clearPendingProductions() {
        pendingProductions.clear();
    }

    public void setPlayersToStart(int playersToStart) {
        getView().getClient().send(new PlayersToStartMessage(playersToStart));
    }

    public void setGameConfig(File gameConfig) {
        String serializedGameConfig = null;
        if(gameConfig != null) {
            try {
                FileInputStream is = new FileInputStream(gameConfig);
                byte[] encoded = is.readAllBytes();
                serializedGameConfig = new String(encoded, StandardCharsets.UTF_8);
            } catch (IOException e) {
                getView().getRenderer().showErrorMessage("The given path is not valid!");
            }
        }

        getView().getClient().send(new GameConfigMessage(serializedGameConfig));
    }

    /**
     *  Buys the DevelopmentCard selected from the deck.
     *
     * @param cardIndex the index of the card to buy
     * @param slotIndex the slot in which the card will be put in
     */

    public abstract void buyDevelopmentCard(int cardIndex, int slotIndex);

    /**
     * Choose a column or row of Marbles in the Market and take all the Resources displayed in the chosen column
     * or row, each Market Marble indicates a Resource.
     *
     * @param marketIndex the index representing the row or column to drawn
     * @param whiteConversions special leader ability that when you take Resources from the market, each
     *                         white Marble in the chosen row or column gives you the indicated Resource
     */

    public abstract void draw(int marketIndex, List<Resource> whiteConversions);

    /**
     * Discards a Leader : you can discard a Leader Card from your hand to receive one Faith Point.
     *
     * @param cardIndex the index of the card to discard
     */

    public abstract void discard(int cardIndex);

    /**
     * Moves the Resources in your deposit: you can move Resources in your depots in any way during your turn.
     * Swaps the resources in {@param row1} and {@param row2}.
     *
     * @param row1 the index of the row of your deposit to be moved
     * @param row2 the index of the row of your deposit where {@param row2} will be moved
     */

    public abstract void move(int row1, int row2);

    /**
     * Places the Resources taken from the Market  in the deposit with the exception of Faith Points,
     * which are registered on the faith Track. The deposit is divided in three depots of 1,2, or 3 slot each.
     * You can place only one type of Resource in a single depot.
     * You can't place the same type of Resource in two different depots.
     * In other words each depot must have the same Resource and all depots must have different Resource.
     *
     * @param resourceIndex thh index representing the Resource to store
     * @param rowIndex the index representing the row of the deposit where the Resource will be stored
     */

    public abstract void storeMarketResult(int resourceIndex, int rowIndex);

    public void endTurn() {
        if(!view.isOwnTurn()) {
            view.getRenderer().showErrorMessage(ViewString.NOT_YOUR_TURN);
            return;
        }

        view.getClient().send(new EndTurnPlayerActionEvent());
        view.setUsingProductions(false);
    }

    /**
     * Activates a LeaderCard:  If you satisfy the requirement of a Leader Card in your hand, you can play
     that LeaderCard. It will give you a special ability for the rest of the game.
     *
     * @param cardIndex the index of the LeaderCard to set active
     */

    public abstract void setActive(int cardIndex);

    /**
     * Adds the the production ability of a LeaderCard to the queue. This ability gives you an additional production power.
     * When you activate the production, you can freely use this power as usual.
     * You will receive a Resource of your choosing and 1 Faith Point.
     *
     * @param cardIndex the index of the card to be used
     * @param desiredResource the desired Resource of your choosing to receive
     */

    public abstract void useLeaderProduction(int cardIndex, Resource desiredResource);

    /**
     * Adds the production ability of a DevelopmentCard to the queue. Every Development Card has a production power.
     * When you activate the production, you can pay the Resources required to receive the Resources given by the
     * LeaderCard.
     *
     * @param cardIndex the index of the DevelopmentCard to use
     */

    public abstract void useDevelopmentProduction(int cardIndex);

    /**
     * Adds the base production ability to the queue. The basic production power allows you to pay 2 Resources of
     * any type (even 2 different Resources) to receive 1 Resource of your choosing.
     *
     * @param inputResource a List representing the Resources needed to activate the base production.
     * @param outputResource a Resource representing the output of the base production.
     */

    public abstract void useBaseProduction(List<Resource> inputResource, List<Resource> outputResource);

    /**
     * Checks if the development productions and the base productions in the queue can be executed and, if so,
     * executes them.
     */

    public abstract void executeProductions();

    /**
     * Sends a given message to all the players.
     *
     * @param message the message to send
     */

    public abstract void sendChatMessage(String message);
}
