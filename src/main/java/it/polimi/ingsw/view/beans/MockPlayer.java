package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Local copy of a player.
 */
public class MockPlayer {
    private final String name;
    private final boolean localPlayer;
    private final IntegerProperty popeFavours;
    private final IntegerProperty faithPoints;

    private int initialResourcesToChoose;

    private final ObservableMap<LeaderCard, Boolean> leaderCards;
    private final MockPlayerBoard playerBoard;

    public MockPlayer(String name, boolean localPlayer) {
        this.name = name;
        this.localPlayer = localPlayer;
        faithPoints = new SimpleIntegerProperty(0);
        this.popeFavours = new SimpleIntegerProperty(0);
        this.leaderCards = FXCollections.observableHashMap();
        this.playerBoard = new MockPlayerBoard(this);
    }

    /**
     * Checks if this MockPlayer is the player that is local to this instance of the client.
     *
     * @return true if this is the local player, false otherwise
     */
    public boolean isLocalPlayer() {
        return localPlayer;
    }

    public String getName() {
        return name;
    }

    /**
     * Gets faith points of the player.
     *
     * @return the number of faith points held by the player
     */
    public int getFaithPoints() {
        return this.faithPoints.get();
    }

    /**
     * Sets the faith points of the player.
     *
     * @param faithPoints the number of faith points to set
     */
    public void setFaithPoints(int faithPoints) {
        this.faithPoints.set(faithPoints);
    }

    /**
     * Gets the number of pope favours of the player.
     *
     * @return the number of pope favours of the player
     */
    public int getPopeFavours() {
        return popeFavours.get();
    }

    /**
     * Sets the number of pope favours of the player.
     *
     * @param popeFavours the new number of pope favours of the player
     */
    public void setPopeFavours(int popeFavours) {
        this.popeFavours.set(popeFavours);
    }

    public IntegerProperty popeFavoursProperty() {
        return popeFavours;
    }

    /**
     * Gets a map corresponding to the LeaderCards of the player.
     *
     * @return a map of the LeaderCards.
     */
    public Map<LeaderCard, Boolean> getLeaderCards() {
        return leaderCards;
    }

    /**
     * Gets an observable map corresponding to the LeaderCards of the player.
     *
     * @return an observable map of the LeaderCards.
     */
    public ObservableMap<LeaderCard, Boolean> leaderCardsProperty() {
        return leaderCards;
    }

    /**
     * Gets the LeaderCard at the given index.
     *
     * @param index the index of the leader card (should be 0 or 1)
     * @return the leader card at the given index, or null if not found
     */
    public LeaderCard getLeaderCardAt(int index) {
        ArrayList<LeaderCard> leaderCards = new ArrayList<>(getLeaderCards().keySet());
        try {
            return leaderCards.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;//TODO Check if this return is considered
        }
    }

    /**
     * Checks if the given LeaderCard is active.
     *
     * @param leaderCard the leader card to check
     * @return true if it's active, false otherwise
     */
    public boolean isLeaderCardActive(LeaderCard leaderCard) {
        return leaderCards.getOrDefault(leaderCard, false);
    }

    /**
     * Sets the LeaderCards of the player.
     *
     * @param leaderCards a map containing the LeaderCards that need to be set
     */
    public void setLeaderCards(Map<LeaderCard, Boolean> leaderCards) {
        this.leaderCards.clear();
        this.leaderCards.putAll(leaderCards);
    }

    /**
     * Checks if the player has two leader cards with deposit as their special ability type.
     *
     * @return true if the player has two leader cards with deposit as their special ability type,
     * else false
     */
    public boolean hasTwoLeaderDeposits() {
        int count = 0;
        for (LeaderCard card : leaderCards.keySet()) {
            if (card.getSpecialAbility().getType() == SpecialAbilityType.DEPOT) count++;
        }
        return count == 2;
    }

    /**
     * Gets the MockPlayerBoard associated with this player.
     *
     * @return the player board of this player
     */
    public MockPlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Shortcut for {@link MockPlayerBoard#getDeposit()}.
     *
     * @return the MockDeposit of this player
     */
    public MockDeposit getDeposit() {
        return playerBoard.getDeposit();
    }

    /**
     * Gets the amount of resources that this player can choose in its first turn.
     *
     * @return the amount of initial resources to choose
     */
    public int getInitialResourcesToChoose() {
        return initialResourcesToChoose;
    }

    /**
     * Sets the amount of resources that this player can choose in its first turn.
     *
     * @param resourceToChoose the amount of initial resources to choose
     */
    public void setInitialResourcesToChoose(int resourceToChoose) {
        this.initialResourcesToChoose = resourceToChoose;
    }

    public IntegerProperty faithPointsProperty() {
        return faithPoints;
    }

    public List<Resource> getExchangeAbility() {
        List<Resource> resources = new ArrayList<>();
        for (LeaderCard card : leaderCards.keySet()) {
            if (leaderCards.get(card) && card.getSpecialAbility().getType() == SpecialAbilityType.EXCHANGE) {
                resources.add(card.getSpecialAbility().getTargetResource());
            }
        }
        return resources;
    }
}
