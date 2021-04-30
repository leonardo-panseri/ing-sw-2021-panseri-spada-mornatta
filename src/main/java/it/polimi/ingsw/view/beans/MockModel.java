package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import it.polimi.ingsw.view.beans.MockDeposit;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.beans.MockPlayerBoard;

import java.util.*;

public class MockModel {
    private MockPlayer localPlayer;
    private final Map<String, MockPlayer> players;

    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck;
    private List<List<Resource>> market;

    /**
     * Initializes all the necessary attributes of the MockModel.
     */
    public MockModel() {
        localPlayer = null;
        players = new HashMap<>();
    }

    public MockPlayer getLocalPlayer() {
        if(localPlayer == null)
            System.err.println("Tried to get local player but it was not set");
        return localPlayer;
    }

    public void setLocalPlayer(MockPlayer localPlayer) {
        this.localPlayer = localPlayer;
    }

    public MockPlayer getPlayer(String name) {
        return players.getOrDefault(name.toLowerCase(), null);
    }

    public MockPlayer addPlayer(String name, boolean localPlayer) {
        if(!players.containsKey(name.toLowerCase())) {
            MockPlayer newPlayer = new MockPlayer(name, localPlayer);
            players.put(name.toLowerCase(), newPlayer);
            return newPlayer;
        } else
            return players.get(name.toLowerCase());
    }

    /**
     * Gets the deck made up of development cards.
     *
     * @return a list representing the deck made up of development cards
     */
    public List<HashMap<CardColor, Stack<DevelopmentCard>>> getDevelopmentDeck() {
        return developmentDeck;
    }

    /**
     * Sets the deck made up of development cards.
     *
     * @param developmentDeck a stack representing the deck made up of development cards, each stack initially has
     *                        3 cards
     */
    public void setDevelopmentDeck(List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck) {
        this.developmentDeck = developmentDeck;
    }

    /**
     * Gets the market.
     *
     * @return a list of a list representing the market used to draw resources
     */
    public List<List<Resource>> getMarket() {
        return market;
    }

    /**
     * Sets the market.
     *
     * @param market list of a list representing the market used to draw resources
     */
    public void setMarket(List<List<Resource>> market) {
        this.market = market;
    }

    /**
     * Updates a row of the market.
     *
     * @param index the index representing the row that needs to be changed
     * @param changes a List containing the resources that will be associated to the new row
     */
    public void updateMarketRow(int index, List<Resource> changes) {
        getMarket().get(index).clear();
        getMarket().get(index).addAll(changes);
    }

    /**
     * Update a column of the market.
     *
     * @param index the index representing the column that needs to be changed
     * @param changes a List containing the resources that will be associated to the new column
     */
    public void updateMarketColumn(int index, List<Resource> changes) {
        for (List<Resource> row : getMarket()) {
            row.remove(index);
            row.add(index, changes.get(getMarket().indexOf(row)));
        }
    }

    /**
     * Counts the occurrences of a white resource in a given row or column of the market.
     *
     * @param index the index representing the row or column of the market
     * @return the occurrences of a white resource in the row or column of the market
     */
    public int countWhiteResources(int index) {
        int result = 0;
        if (index > 0 && index < 5) {
            for (int i = 0; i < 3; i++) {
                if (market.get(i).get(index - 1) == null) result++;
            }
        } else if (index > 4 && index < 8) {
            for (List<Resource> row : market) {
                for (Resource res : row) {
                    if (res == null) result++;
                }
            }
        }
        return result;
    }


}
