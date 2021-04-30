package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockDeposit {
    private final MockPlayer player;

    private List<List<Resource>> deposit;
    private Map<Resource, Integer> strongbox;
    private Map<Integer, List<Resource>> leadersDeposit;
    private List<Resource> marketResult;

    public MockDeposit(MockPlayer player) {
        this.player = player;
        deposit = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            deposit.add(new ArrayList<>());
        }
        strongbox = new HashMap<>();
        leadersDeposit = new HashMap<>();
        leadersDeposit.put(1, new ArrayList<>());
        leadersDeposit.put(2, new ArrayList<>());
        marketResult = new ArrayList<>();
    }

    public void setRow(int row, List<Resource> deposit) {
        this.deposit.set(row, deposit);
    }

    public List<Resource> getRow(int index) {
        return deposit.get(index);
    }

    public List<List<Resource>> getAllRows() {
        return deposit;
    }

    /**
     * Gets the strongbox of the current player.
     *
     * @return a map showing the number of times that the resources are present in the strongbox
     */
    public Map<Resource, Integer> getStrongbox() {
        return strongbox;
    }


    /**
     * Sets the strongbox of the current player.
     *
     * @param strongbox a Map representing the strongbox, the integer
     *                  represents the number of times the resource is present in the strongbox
     */
    public void setStrongbox(Map<Resource, Integer> strongbox) {
        this.strongbox = strongbox;
    }

    /**
     * Gets the deposit of the leader cards having deposit as their special ability.
     *
     * @return a Map showing the deposit of the leaders
     */
    public List<Resource> getLeadersDeposit(int index) {
        if(index != 1 && index != 2) {
            System.err.println("Tried to get a leader deposit with an incorrect index");
            return null;
        }
        return leadersDeposit.get(index);
    }

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
