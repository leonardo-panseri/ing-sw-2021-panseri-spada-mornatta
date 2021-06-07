package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.messages.DepositStrongboxUpdate;
import it.polimi.ingsw.model.messages.DepositUpdate;
import it.polimi.ingsw.model.messages.MarketResultUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.server.IServerPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models the deposit of a player board, comprehensive of the three rows of resources slots, adn the strongbox.
 * Rows are indexed starting from 1 and ending to 3, from top to bottom. The eventual leaders deposit are represented by 4.
 */
public class Deposit extends Observable<IServerPacket> {
    private final Player player;
    private Resource topRow;
    private List<Resource> middleRow;
    private List<Resource> bottomRow;
    private final Map<Resource, Integer> strongBox;
    /**
     * Key can be 1 or 2 and indicates leader card slot, the list contains a maximum of 2 resources of the same type
     * and is the content of the leader card deposit
     */
    private final Map<Integer, List<Resource>> leadersDeposit;
    private final List<Resource> marketResults;

    /**
     * Getter: gets the requested row of resources.
     *
     * @param row the index of the wanted row of resources
     * @return a list with the resources
     */
    List<Resource> getRow(int row) {
        if (row == 1) {
            List<Resource> result = new ArrayList<>();
            if (topRow != null) result.add(topRow);
            return result;
        } else if (row == 2) {
            return middleRow;
        } else if (row == 3) {
            return bottomRow;
        }
        notifyDepositUpdate(row);
        return null;
    }

    /**
     * Gets the number representing the row where the resource is stored.
     *
     * @param res the resource to find.
     * @return the int representing the row where the resource is stored. Returns -1 if the resource is not present.
     */

    int findResource(Resource res) {
        if (topRow == res) return 1;
        if (middleRow.contains(res)) return 2;
        if (bottomRow.contains(res)) return 3;
        if (leadersDeposit.get(1).contains(res)) return 4;
        if (leadersDeposit.get(2).contains(res)) return 5;
        if (strongBox.containsKey(res)) {
            if (strongBox.get(res) > 0) return 6;
        }
        return -1;
    }

    /**
     * Getter: gets the strongbox.
     *
     * @return the strongbox
     */
    Map<Resource, Integer> getStrongBox() {
        return strongBox;
    }

    /**
     * Constructor: instantiates the three rows of slots, and store the owner of the player board.
     *
     * @param player the player owner of the board.
     */
    Deposit(Player player) {
        middleRow = new ArrayList<>();
        bottomRow = new ArrayList<>();
        strongBox = new HashMap<>();
        leadersDeposit = new HashMap<>();
        leadersDeposit.put(1, new ArrayList<>());
        leadersDeposit.put(2, new ArrayList<>());
        marketResults = new ArrayList<>();
        this.player = player;
    }

    /**
     * Gets the total amount of the given resource in this player board.
     *
     * @param resource the resource to count
     * @return the total amount of the given resource
     */
    public synchronized int getAmountOfResource(Resource resource) {
        int amount = 0;
        if (topRow == resource)
            amount++;
        for (Resource res : middleRow)
            if (res == resource)
                amount++;
        for (Resource res : bottomRow)
            if (res == resource)
                amount++;
        amount += strongBox.getOrDefault(resource, 0);
        for (Resource res : getMarketResults())
            if (res == resource)
                amount++;
        for (int leaderSlot : leadersDeposit.keySet())
            for (Resource res : leadersDeposit.get(leaderSlot))
                if (res == resource)
                    amount++;
        return amount;
    }

    /**
     * Set the initial resources chosen by the Player.
     *
     * @param selectedResources the resources that the player has selected
     */
    public void setInitialResources(Map<Integer, List<Resource>> selectedResources) {
        checkBoundaries(selectedResources);

        setNewRows(selectedResources);
    }

    /**
     * Applies the given changes to this player deposit, checking if they are legal.
     *
     * @param changes        a map representing changes to be applied, the key is the identifier of the row (1 -> top,
     *                       2 -> middle, 3 -> bottom), the value is the list of resources that represents the new row
     * @param marketResult   a list containing the possibly modified market result
     * @param leadersDeposit a map representing the leader deposits
     * @throws IllegalArgumentException if the changes are not legal
     */
    public void applyChanges(Map<Integer, List<Resource>> changes, List<Resource> marketResult, Map<Integer, List<Resource>> leadersDeposit) throws IllegalArgumentException {
        checkQuantities(changes, marketResult, leadersDeposit);
        checkBoundaries(changes);
        checkLeaderDeposit(leadersDeposit);

        leadersDeposit.forEach((leaderSlot, resourceList) ->
                this.leadersDeposit.merge(leaderSlot, resourceList, (originalList, modifiedList) -> modifiedList));

        setNewRows(changes);
    }

    /**
     * Overrides current deposit rows with new ones.
     *
     * @param changes a map representing the changes to be made to the deposit
     */
    private void setNewRows(Map<Integer, List<Resource>> changes) {
        int modifiedLength = changes.keySet().size();
        int[] modifiedRows = new int[modifiedLength];
        int index = 0;
        for (int i = 1; i < 4; i++) {
            if (changes.containsKey(i)) {
                if (i == 1) {
                    if (!changes.get(i).isEmpty()) topRow = changes.get(i).get(0);
                    else topRow = null;
                } else if (i == 2) middleRow = new ArrayList<>(changes.get(i));
                else bottomRow = new ArrayList<>(changes.get(i));
                modifiedRows[index] = i;
                index++;
            }
        }

        notifyDepositUpdate(modifiedRows);
    }

    /**
     * Checks if the deposit move is legal.
     *
     * @param changes        a map representing changes to be applied, the key is the identifier of the row (1 -> top,
     *                       2 -> middle, 3 -> bottom), the value is the list of resources that represents the new row
     * @param marketResult   a list containing the possibly modified market result
     * @param leadersDeposit a map containing the new leaders deposits
     * @throws IllegalArgumentException if the changes are not legal
     */
    private void checkQuantities(Map<Integer, List<Resource>> changes, List<Resource> marketResult, Map<Integer, List<Resource>> leadersDeposit) throws IllegalArgumentException {
        List<Integer> untouchedRows = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            if (!changes.containsKey(i)) untouchedRows.add(i);
        }

        //Mapping the old quantities of resources in deposit, strongbox and market result
        Map<Resource, Integer> oldResources = new HashMap<>();
        for (Resource res : Resource.values()) {
            oldResources.put(res, getAmountOfResource(res));
        }

        //Mapping the new quantities of resources in deposit, strongbox and market result
        //Assembly the new deposit adding the untouched rows
        Map<Resource, Integer> newResources = new HashMap<>();
        for (int missingRow : untouchedRows) {
            changes.put(missingRow, getRow(missingRow));
        }
        for (List<Resource> row : changes.values()) {
            for (Resource res : row) {
                if (newResources.containsKey(res)) {
                    newResources.put(res, newResources.get(res) + 1);
                } else {
                    newResources.put(res, 1);
                }
            }
        }

        //Add quantities from the strongbox (not modified by the action)
        for (Resource res : getStrongBox().keySet()) {
            if (newResources.containsKey(res)) {
                newResources.put(res, newResources.get(res) + getStrongBox().get(res));
            } else {
                newResources.put(res, getStrongBox().get(res));
            }
        }

        //Add quantities from the market result list
        for (Resource res : marketResult) {
            if (newResources.containsKey(res)) {
                newResources.put(res, newResources.get(res) + 1);
            } else {
                newResources.put(res, 1);
            }
        }

        //Add quantities from the leaders deposits
        if (!leadersDeposit.containsKey(1)) leadersDeposit.put(1, this.leadersDeposit.get(1));
        if (!leadersDeposit.containsKey(2)) leadersDeposit.put(2, this.leadersDeposit.get(2));
        for (int leaderSlot : leadersDeposit.keySet()) {
            for (Resource res : leadersDeposit.get(leaderSlot))
                if (newResources.containsKey(res)) {
                    newResources.put(res, newResources.get(res) + 1);
                } else {
                    newResources.put(res, 1);
                }
        }

        for (Resource res : Resource.values()) {
            if (!newResources.containsKey(res)) newResources.put(res, 0);
        }

        //At this point, if the move is legal, the two maps should be the same
        for (Resource res : Resource.values()) {
            if (!oldResources.get(res).equals(newResources.get(res))) {
                throw new IllegalArgumentException("Resource " + res + " quantity is not valid!");
            }
        }
    }

    /**
     * Checks if the given changes are compliant with game rules.
     *
     * @param changes a map representing changes to be applied, the key is the identifier of the row (1 -> top,
     *                2 -> middle, 3 -> bottom), the value is the list of resources that represents the new row
     */
    private void checkBoundaries(Map<Integer, List<Resource>> changes) {
        if (changes.containsKey(1) && changes.get(1).size() > 1)
            throw new IllegalArgumentException("Deposit top row is already full!");
        if (changes.containsKey(2) && changes.get(2).size() > 2)
            throw new IllegalArgumentException("Deposit middle row is already full!");
        if (changes.containsKey(3) && changes.get(3).size() > 3)
            throw new IllegalArgumentException("Deposit bottom row is already full!");

        List<Resource> alreadySeen = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            List<Resource> row;
            if (!changes.containsKey(i)) {
                row = getRow(i);
            } else row = changes.get(i);
            if (row.size() > 0) {
                Resource first = row.get(0);
                if (alreadySeen.contains(first))
                    throw new IllegalArgumentException("Resource " + first + " is already stored in another row!");
                alreadySeen.add(first);
                for (int j = 1; j < row.size(); j++)
                    if (row.get(j) != first)
                        throw new IllegalArgumentException("Row " + j + " contains mismatched resources");
            }
        }
    }

    /**
     * Checks if the given leaders deposits are compliant to game rules.
     *
     * @param leadersDeposit a map representing leaders deposits, the keys should be only 1 and/or 2, representing
     *                       the modified leader deposits
     * @throws IllegalArgumentException if the given changes violate game rules
     */
    private void checkLeaderDeposit(Map<Integer, List<Resource>> leadersDeposit) throws IllegalArgumentException {
        List<Resource> depositsRequired = new ArrayList<>();
        for (int leaderSlot : leadersDeposit.keySet()) {
            int amount = 0;
            Resource resource;
            if (!leadersDeposit.get(leaderSlot).isEmpty()) {
                resource = leadersDeposit.get(leaderSlot).get(0);
                depositsRequired.add(resource);
                for (Resource res : leadersDeposit.get(leaderSlot)) {
                    amount++;
                    if (res != resource)
                        throw new IllegalArgumentException("Leader deposit " + leaderSlot + " contains mismatched resources!");
                }
                if (amount > 2)
                    throw new IllegalArgumentException("Leader deposit " + leaderSlot + " is already full!");
            }
        }
        if (!player.hasLeaderDeposits(depositsRequired))
            throw new IllegalArgumentException("You cannot use this leader deposit because you don't have an active leader card with this special ability!");
    }

    /**
     * Adds the desired resource into a free slot on the indicated row, if the move if possible.
     *
     * @param row      the row where to insert the resource in, 1 is the top row, 2 is the middle row and 3 is the bottom row
     * @param resource the resource to insert
     */
    void addResource(int row, Resource resource) {
        if (row == 1 && topRow == null) {
            topRow = resource;
        } else if (row == 2 && middleRow.size() < 2) {
            middleRow.add(resource);
        } else if (row == 3 && bottomRow.size() < 3) {
            bottomRow.add(resource);
        }
        notifyDepositUpdate(row);
    }

    /**
     * Remove a resource of the desired type from the indicated row, if existing.
     *
     * @param row      the row where to delete the resource from
     * @param resource the resource to delete
     * @return 1 if the resource has been removed, 0 otherwise
     */
    int removeResource(int row, Resource resource) {
        if (row == 1 && topRow != null) {
            topRow = null;
            return 1;
        } else if (row == 2 && middleRow.size() > 0) {
            if (middleRow.remove(resource)) return 1;
        } else if (row == 3 && bottomRow.size() > 0) {
            if (bottomRow.remove(resource)) return 1;
        } else if (row == 4) {
            if (leadersDeposit.get(1).remove(resource)) return 1;
        } else if (row == 5) {
            if (leadersDeposit.get(2).remove(resource)) return 1;
        } else if (row == 6) {
            if (removeFromStrongbox(resource)) return 1;
        }

        return 0;
    }

    /**
     * Removes all the given resources from this deposit.
     *
     * @param resources a map that associates resource to quantity
     */
    public synchronized void removeResources(Map<Resource, Integer> resources) {
        List<Integer> changedRows = new ArrayList<>();
        for (Resource res : resources.keySet()) {
            int removed = 0;
            while (removed < resources.get(res)) {
                int row = findResource(res);

                if (row == -1) {
                    System.err.println("Trying to remove too much resources from deposit");
                    break;
                }

                removed += removeResource(row, res);
                if (row != 4 && row != 5 && row != 6) {
                    if (!changedRows.contains(row)) changedRows.add(row);
                }
            }
        }
        int changedLength = changedRows.size();
        int[] rows = new int[changedLength];
        for (int i = 0; i < changedLength; i++) {
            rows[i] = changedRows.get(i);
        }
        notifyDepositUpdate(rows);
    }

    /**
     * Switches two rows of resources, checking if the move is possible.
     *
     * @param toMove      row to move
     * @param destination row to be switched with the previous one
     */
    void moveRow(int toMove, int destination) {
        List<Resource> switchedResources = new ArrayList<>();
        int movedTo = -1;
        switch (toMove) {
            case 1:
                if (topRow != null) {
                    if (destination == 2 && middleRow.size() < 2) {
                        // Top <--> Middle
                        movedTo = 2;
                        switchedResources.addAll(middleRow);
                        middleRow.clear();
                        middleRow.add(topRow);
                        topRow = switchedResources.get(0);
                    } else if (destination == 3 && bottomRow.size() < 2) {
                        // Top <--> Bottom
                        movedTo = 3;
                        switchedResources.addAll(bottomRow);
                        bottomRow.clear();
                        bottomRow.add(topRow);
                        topRow = switchedResources.get(0);
                    }
                }
                break;

            case 2:
                if (middleRow != null) {
                    if (destination == 1 && middleRow.size() < 2) {
                        // Middle <--> Top
                        movedTo = 1;
                        if (topRow != null) switchedResources.add(topRow);
                        topRow = middleRow.get(0);
                        middleRow = null;
                        middleRow = switchedResources;
                    } else if (destination == 3 && bottomRow.size() < 3) {
                        // Middle <--> Bottom
                        movedTo = 3;
                        switchedResources = bottomRow;
                        bottomRow = middleRow;
                        middleRow = null;
                        middleRow = switchedResources;
                    }
                }
                break;

            case 3:
                if (bottomRow != null) {
                    if (destination == 1 && bottomRow.size() < 2) {
                        // Bottom <--> Top
                        movedTo = 1;
                        if (topRow != null) switchedResources.add(topRow);
                        topRow = bottomRow.get(0);
                        bottomRow = null;
                        bottomRow = switchedResources;
                    } else if (destination == 2 && bottomRow.size() < 3) {
                        // Bottom <--> Middle
                        movedTo = 2;
                        switchedResources = middleRow;
                        middleRow = bottomRow;
                        bottomRow = null;
                        bottomRow = switchedResources;
                    }
                }
                break;
        }
        if (movedTo != -1) {
            notifyDepositUpdate(1, movedTo);
        }
    }

    /**
     * Notifies observers of deposit update.
     *
     * @param rows the rows that have been modified
     */
    private void notifyDepositUpdate(int... rows) {
        HashMap<Integer, List<Resource>> changes = new HashMap<>();
        for (int i : rows) {
            List<Resource> row = null;
            switch (i) {
                case 1 -> {
                    row = new ArrayList<>();
                    if (topRow != null) row.add(topRow);
                }
                case 2 -> row = middleRow;
                case 3 -> row = bottomRow;
            }
            if (row != null) {
                changes.put(i, row);
            }
        }

        notify(new DepositUpdate(player.getNick(), changes, leadersDeposit));
    }

    /**
     * Adds the desired resource into the strongbox, modeled by a map of resources and integers.
     * If the type of resource is already stored, it adds one to the current related counter.
     *
     * @param resource the resource to be added into the strongbox
     */
    public synchronized void addToStrongbox(Resource resource) {
        int counter = 1;
        if (strongBox.containsKey(resource)) {
            counter = strongBox.get(resource) + 1;
        }
        strongBox.put(resource, counter);
        notifyStrongboxUpdate();
    }

    /**
     * Adds the desired map of resources into the strongbox, modeled by a map of resources and integers.
     * If the type of resource is already stored, it adds one to the current related counter.
     *
     * @param resources the map of resources to be added in the strongbox
     */
    public void addMultipleToStrongbox(Map<Resource, Integer> resources) {
        for (Resource res : resources.keySet()) {
            for (int i = 0; i < resources.get(res); i++) {
                addToStrongbox(res);
            }
        }
    }

    /**
     * Removes the desired resource into the strongbox, modeled by a map of resources and integers.
     * If the type of resource is stored, it removes one to the current related counter.
     *
     * @param resource the resource to be removed
     * @return true if the resource has been removed, false otherwise
     */
    boolean removeFromStrongbox(Resource resource) {
        int counter;
        if (strongBox.containsKey(resource) && strongBox.get(resource) > 0) {
            counter = strongBox.get(resource) - 1;
            strongBox.put(resource, counter);
            notifyStrongboxUpdate();
            return true;
        }
        return false;
    }

    /**
     * Notifies observers of strongbox update.
     */
    private synchronized void notifyStrongboxUpdate() {
        notify(new DepositStrongboxUpdate(player.getNick(), strongBox));
    }

    /**
     * Sets the market results of this {@link Player} PlayerBoard.
     *
     * @param marketResults an arraylist containing the market results
     */
    public void setMarketResults(List<Resource> marketResults) {
        this.marketResults.clear();
        this.marketResults.addAll(marketResults);
        notify(new MarketResultUpdate(player.getNick(), this.marketResults));
    }

    /**
     * Getter for the market result.
     *
     * @return a list of resources drawn from the market
     */
    public List<Resource> getMarketResults() {
        return marketResults;
    }

    /**
     * Gets the amount of resources in the market result slots.
     *
     * @return the amount of resources stored in the market result slots
     */
    public int getUnusedMarketResults() {
        return marketResults.size();
    }

    /**
     * Clears the market result slots.
     */
    public void clearMarketResults() {
        marketResults.clear();
        notify(new MarketResultUpdate(player.getNick(), marketResults));
    }

    /**
     * Counts all resources present in this deposit.
     *
     * @return the total amount of resources present
     */
    public int countAllResources() {
        int amount = 0;
        amount += topRow != null ? 1 : 0;
        amount += middleRow.size() + bottomRow.size();
        amount += strongBox.values().stream().reduce(0, Integer::sum);
        amount += leadersDeposit.values().stream().mapToInt(List::size).reduce(0, Integer::sum);
        return amount;
    }
}
