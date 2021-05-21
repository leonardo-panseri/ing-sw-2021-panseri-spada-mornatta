package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.server.GameConfig;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.*;

/**
 * Local copy of the game model.
 */
public class MockModel {
    private IntegerProperty currentPlayers;
    private IntegerProperty playersToStart;

    private MockPlayer localPlayer;
    private final ObservableMap<String, MockPlayer> players;

    private GameConfig gameConfig;
    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck;
    private final MockMarket market;

    /**
     * Constructs a new MockModel initializing the players map and the market, and setting the local player to null.
     */
    public MockModel() {
        currentPlayers = new SimpleIntegerProperty(-1);
        playersToStart = new SimpleIntegerProperty(-1);

        localPlayer = null;
        players = FXCollections.observableHashMap();
        market = new MockMarket();
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    /**
     * Gets the MockPlayer that is playing with this instance of the client.
     *
     * @return the local player, if it has not been set yet returns null
     */
    public MockPlayer getLocalPlayer() {
        if(localPlayer == null)
            System.err.println("Tried to get local player but it was not set");
        return localPlayer;
    }

    /**
     * Sets the MockPlayer that is playing with this instance of the client.
     *
     * @param localPlayer the local player to be set
     */
    public void setLocalPlayer(MockPlayer localPlayer) {
        this.localPlayer = localPlayer;
    }

    /**
     * Gets the MockPlayer with the given name, if not found returns null.
     *
     * @param name the name of the player to search for
     * @return the player with the given name if found, null otherwise
     */
    public MockPlayer getPlayer(String name) {
        return players.getOrDefault(name.toLowerCase(), null);
    }

    /**
     * Creates a new MockPlayer and adds it to the player map, if a player with this name is already present returns it
     * instead.
     *
     * @param name the name of the player
     * @param localPlayer if this player should be set as the local player
     * @return the new player added or the existing one with this name if found
     */
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
    public MockMarket getMarket() {
        return market;
    }

    public IntegerProperty currentPlayersProperty() {
        return currentPlayers;
    }

    public IntegerProperty playersToStartProperty() {
        return playersToStart;
    }

    public ObservableMap<String, MockPlayer> getPlayers() {
        return players;
    }

    public void updatePlayerCount(int currentPlayers, int playersToStart) {
        this.currentPlayers.setValue(currentPlayers);
        this.playersToStart.setValue(playersToStart);
    }
}
