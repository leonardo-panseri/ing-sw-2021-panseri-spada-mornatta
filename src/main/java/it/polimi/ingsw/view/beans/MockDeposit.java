package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import it.polimi.ingsw.view.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.*;

/**
 * Local copy of a Player's deposit.
 */
public class MockDeposit {
    private final MockPlayer player;

    private final ObservableList<List<Resource>> deposit;
    private final ObservableMap<Resource, Integer> strongbox;
    private final ObservableMap<Integer, List<Resource>> leadersDeposit;
    private final ObservableList<Resource> marketResult;

    private final Map<UUID, Integer> leaderCardToDepositLink;

    private final List<List<Resource>> depositBackup;
    private final Map<Resource, Integer> strongboxBackup;
    private final Map<Integer, List<Resource>> leadersDepositBackup;

    /**
     * Constructs a new empty MockDeposit for the given MockPlayer.
     *
     * @param player the player owner of this deposit
     */
    public MockDeposit(MockPlayer player) {
        this.player = player;
        deposit = FXCollections.observableList(Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        strongbox = FXCollections.observableHashMap();
        leadersDeposit = FXCollections.observableHashMap();
        leadersDeposit.put(1, new ArrayList<>());
        leadersDeposit.put(2, new ArrayList<>());
        marketResult = FXCollections.observableArrayList();

        this.leaderCardToDepositLink = new HashMap<>();

        this.depositBackup = new ArrayList<>();
        this.strongboxBackup = new HashMap<>();
        this.leadersDepositBackup = new HashMap<>();
    }

    /**
     * Sets the given resources in the row with the given index.
     *
     * @param index   the index of the row to change (0 -> top row, 1 -> middle row, 2 -> bottom row)
     * @param changes the new row to override to the row with the id
     */
    public void setRow(int index, List<Resource> changes) {
        this.deposit.set(index, changes);
    }

    public boolean addToRow(int index, Resource resource) {
        if (deposit.get(index).size() >= index + 1)
            return false;
        List<Resource> row = deposit.get(index);
        if (row.size() > 0)
            if (resource != row.get(0))
                return false;
        row.add(resource);
        setRow(index, row);
        return true;
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
    public ObservableMap<Resource, Integer> strongBoxProperty() {
        return strongbox;
    }


    /**
     * Sets the strongbox.
     *
     * @param strongbox a map representing the strongbox, the integer
     *                  represents the number of times the resource is present in the strongbox
     */
    public void setStrongbox(Map<Resource, Integer> strongbox) {
        this.strongbox.clear();
        this.strongbox.putAll(strongbox);
    }

    /**
     * Gets the deposit of the leader with the given index.
     *
     * @param index the index of the leader deposit (1 -> first leader deposit, 2 -> second leader deposit)
     * @return a list containing the leader deposit at the given index
     */
    public List<Resource> getLeadersDeposit(int index) {
        if (index != 1 && index != 2) {
            System.err.println("Tried to get a leader deposit with an incorrect index");
            return null;
        }
        return leadersDeposit.get(index);
    }

    /**
     * Gets the active leader deposits.
     *
     * @return a map associating the indexes of the leaders to the corresponding deposits if the leaders are active and
     * have the deposit ability
     */
    public Map<Integer, List<Resource>> getActiveLeadersDeposit() {
        HashMap<Integer, List<Resource>> result = new HashMap<>();
        LeaderCard card1 = player.getLeaderCardAt(0);
        LeaderCard card2 = player.getLeaderCardAt(1);
        if (card1 != null && card1.getSpecialAbility().getType() == SpecialAbilityType.DEPOT && player.isLeaderCardActive(card1))
            result.put(1, leadersDeposit.get(1));
        if (card2 != null && card2.getSpecialAbility().getType() == SpecialAbilityType.DEPOT && player.isLeaderCardActive(card2))
            result.put(2, leadersDeposit.get(2));

        return result;
    }

    /**
     * Sets the deposit of the leader cards having deposit as their special ability.
     *
     * @param leadersDeposit a Map showing the deposit of the leaders
     */
    public void setLeadersDeposit(Map<Integer, List<Resource>> leadersDeposit) {
        this.leadersDeposit.clear();
        this.leadersDeposit.putAll(leadersDeposit);
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
        this.marketResult.setAll(marketResult);
    }

    public ObservableList<Resource> marketResultProperty() {
        return marketResult;
    }

    public ObservableMap<Integer, List<Resource>> leaderDepositProperty() {
        return leadersDeposit;
    }

    public void registerLeaderCardToDeposit(LeaderCard card) {
        if (leaderCardToDepositLink.containsValue(1))
            leaderCardToDepositLink.put(card.getUuid(), 2);
        else
            leaderCardToDepositLink.put(card.getUuid(), 1);
    }

    public int getLeaderDepositIndexForCard(LeaderCard card) {
        return leaderCardToDepositLink.get(card.getUuid());
    }

    public void saveCurrentState() {
        depositBackup.clear();
        for (List<Resource> row : deposit) {
            depositBackup.add(new ArrayList<>(row));
        }
        strongboxBackup.clear();
        strongboxBackup.putAll(strongbox);
        leadersDepositBackup.clear();
        for (Integer index : leadersDeposit.keySet()) {
            leadersDepositBackup.put(index, new ArrayList<>(leadersDeposit.get(index)));
        }
    }

    public void restoreSavedState() {
        for(int i = 0; i < 3; i++) {
            deposit.set(i, depositBackup.get(i));
        }

        strongbox.clear();
        strongbox.putAll(strongboxBackup);

        leadersDeposit.clear();
        leadersDeposit.putAll(leadersDepositBackup);
    }

    public void removeResources(View view, Map<Resource, Integer> resources) {
        for (Resource res : resources.keySet()) {
            int removed = 0;
            while (removed < resources.get(res)) {
                int row = findResource(res);
                if (row == -1) {
                    view.getRenderer().showErrorMessage("You don't have the resources for this production!");
                    break;
                }
                removed += removeResource(row, res);
            }
        }
    }

    int removeResource(int row, Resource resource) {
        if (row >= 1 && row <= 3) {
            List<Resource> newRow = new ArrayList<>(deposit.get(row - 1));
            if (newRow.remove(resource)) {
                deposit.set(row - 1, newRow);
                return 1;
            }
        } else if (row == 4) {
            List<Resource> newRow = new ArrayList<>(leadersDeposit.get(1));
            if (newRow.remove(resource)) {
                leadersDeposit.put(1, newRow);
                return 1;
            }
        } else if (row == 5) {
            List<Resource> newRow = new ArrayList<>(leadersDeposit.get(2));
            if (newRow.remove(resource)) {
                leadersDeposit.put(2, newRow);
                return 1;
            }
        } else if (row == 6) {
            if (strongbox.get(resource) > 0) {
                int newQuantity = strongbox.get(resource) - 1;
                if (newQuantity == 0)
                    strongbox.remove(resource);
                else
                    strongbox.put(resource, newQuantity);
                return 1;
            }
        }
        return 0;
    }

    int findResource(Resource res) {
        for (int i = 0; i < 3; i++) {
            if (deposit.get(i).contains(res))
                return i + 1;
        }
        if (leadersDeposit.get(1).contains(res)) return 4;
        if (leadersDeposit.get(2).contains(res)) return 5;
        if (strongbox.containsKey(res)) {
            if (strongbox.get(res) > 0) return 6;
        }
        return -1;
    }

    public int countResource(Resource resource) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (Resource res : deposit.get(i))
                if (resource == res)
                    count++;
        }
        for (Resource res : leadersDeposit.get(1))
            if (resource == res)
                count++;
        for (Resource res : leadersDeposit.get(2))
            if (resource == res)
                count++;
        if (strongbox.containsKey(resource))
            count += strongbox.get(resource);
        return count;
    }
}
