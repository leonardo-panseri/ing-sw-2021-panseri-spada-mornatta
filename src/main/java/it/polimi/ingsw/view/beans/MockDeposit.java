package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * Local copy of a Player's deposit.
 */
public class MockDeposit {
    private final MockPlayer player;

    private final ObservableList<List<Resource>> deposit;
    private Map<Resource, Integer> strongbox;
    private Map<Integer, List<Resource>> leadersDeposit;
    private List<Resource> marketResult;

    /**
     * Constructs a new empty MockDeposit for the given MockPlayer.
     *
     * @param player the player owner of this deposit
     */
    public MockDeposit(MockPlayer player) {
        this.player = player;
        deposit = FXCollections.observableList(Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        strongbox = new HashMap<>();
        leadersDeposit = new HashMap<>();
        leadersDeposit.put(1, new ArrayList<>());
        leadersDeposit.put(2, new ArrayList<>());
        marketResult = new ArrayList<>();
    }

    /**
     * Sets the given resources in the row with the given index.
     *
     * @param index the index of the row to change (0 -> top row, 1 -> middle row, 2 -> bottom row)
     * @param changes the new row to override to the row with the id
     */
    public void setRow(int index, List<Resource> changes) {
        this.deposit.set(index, changes);
    }

    /**
     * Gets the row at the given index.
     *
     * @param index the index of the row to get (0 -> top row, 1 -> middle row, 2 -> bottom row)
     * @return the row with the given index
     */
    public List<Resource> getRow(int index) {
        return deposit.get(index);
    }

    /**
     * Gets all the rows of this MockDeposit.
     *
     * @return a list containing all rows (list[0] -> top row, list[1] -> middle row, list[2] -> bottom row)
     */
    public List<List<Resource>> getAllRows() {
        return deposit;
    }

    public ObservableList<List<Resource>> depositProperty() {
        return deposit;
    }

    /**
     * Gets the strongbox of this MockDeposit.
     *
     * @return a map associating the resources with the number of that type of resource that is present in the strongbox
     */
    public Map<Resource, Integer> getStrongbox() {
        return strongbox;
    }


    /**
     * Sets the strongbox.
     *
     * @param strongbox a map representing the strongbox, the integer
     *                  represents the number of times the resource is present in the strongbox
     */
    public void setStrongbox(Map<Resource, Integer> strongbox) {
        this.strongbox = strongbox;
    }

    /**
     * Gets the deposit of the leader with the given index.
     *
     * @param index the index of the leader deposit (1 -> first leader deposit, 2 -> second leader deposit)
     * @return a list containing the leader deposit at the given index
     */
    public List<Resource> getLeadersDeposit(int index) {
        if(index != 1 && index != 2) {
            System.err.println("Tried to get a leader deposit with an incorrect index");
            return null;
        }
        return leadersDeposit.get(index);
    }

    /**
     * Gets the active leader deposits.
     *
     * @return a map associating the indexes of the leaders to the corresponding deposits if the leaders are active and
     *         have the deposit ability
     */
    public Map<Integer, List<Resource>> getActiveLeadersDeposit() {
        HashMap<Integer, List<Resource>> result = new HashMap<>();
        LeaderCard card1 = player.getLeaderCardAt(0);
        LeaderCard card2 = player.getLeaderCardAt(1);
        if(card1 != null && card1.getSpecialAbility().getType() == SpecialAbilityType.DEPOT && player.isLeaderCardActive(card1))
            result.put(1, leadersDeposit.get(1));
        if(card2 != null && card2.getSpecialAbility().getType() == SpecialAbilityType.DEPOT && player.isLeaderCardActive(card2))
            result.put(2, leadersDeposit.get(2));

        return result;
    }

    /**
     * Sets the deposit of the leader cards having deposit as their special ability.
     *
     * @param leadersDeposit a Map showing the deposit of the leaders
     */
    public void setLeadersDeposit(Map<Integer, List<Resource>> leadersDeposit) {
        this.leadersDeposit = leadersDeposit;
    }

    /**
     * Gets the result of a draw from the market.
     *
     * @return the result of a draw from the market.
     */
    public List<Resource> getMarketResult() {
        return marketResult;
    }

    /**
     * Sets the result of a draw from the market.
     *
     * @param marketResult a list representing the result of a draw from the market
     */
    public void setMarketResult(List<Resource> marketResult) {
        this.marketResult = marketResult;
    }
}
