package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.messages.DepositStrongboxUpdate;
import it.polimi.ingsw.model.messages.DepositUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.server.IServerPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models the deposit of a player board, comprehensive of the three rows of resources slots, adn the strongbox.
 * Rows are indexed starting from 1 and ending to 3, from top to bottom.
 */
public class Deposit extends Observable<IServerPacket> {
    private final Player player;
    private Resource topRow;
    private List<Resource> middleRow;
    private List<Resource> bottomRow;
    private final Map<Resource, Integer> strongBox;

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
     *
     * @return the int representing the row where the resource is stored.
     */

    int findResource(Resource res) {
        if (topRow == res) return 1;
        if (middleRow.contains(res)) return 2;
        return 3;
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
        for (Resource res : player.getBoard().getMarketResults())
            if (res == resource)
                amount++;
        return amount;
    }

    /**
     * Applies the given changes to this player deposit, checking if they are legal.
     *
     * @param changes a map representing changes to be applied, the key is the identifier of the row (1 -> top,
     *                2 -> middle, 3 -> bottom), the value is the list of resources that represents the new row
     * @param marketResult a list containing the possibly modified market result
     * @throws IllegalArgumentException if the changes are not legal
     */
    public void applyChanges(Map<Integer, List<Resource>> changes, List<Resource> marketResult) throws IllegalArgumentException {
        checkDepositMove(changes, marketResult);
        for (int i = 1; i < 4; i++) {
            if (changes.containsKey(i)) {
                if (i == 1) topRow = changes.get(i).get(0);
                else if (i == 2) middleRow = changes.get(i);
                else bottomRow = changes.get(i);
            }
        }
    }

    /**
     * Checks if the deposit move is legal.
     *
     * @param changes a map representing changes to be applied, the key is the identifier of the row (1 -> top,
     *                2 -> middle, 3 -> bottom), the value is the list of resources that represents the new row
     * @param marketResult a list containing the possibly modified market result
     * @throws IllegalArgumentException if the changes are not legal
     */
    private void checkDepositMove(Map<Integer, List<Resource>> changes, List<Resource> marketResult) throws IllegalArgumentException {
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

        for (Resource res : Resource.values()) {
            if (!newResources.containsKey(res)) newResources.put(res, 0);
        }

        //At this point, if the move is legal, the two maps should be the same

        for (Resource res : Resource.values()) {
            if (!oldResources.get(res).equals(newResources.get(res)))
                throw new IllegalArgumentException("illegal_deposit_move");
        }
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
     */
    void removeResource(int row, Resource resource) {
        if (row == 1 && topRow != null) {
            topRow = null;
        } else if (row == 2 && middleRow.size() > 0) {
            middleRow.remove(resource);
        } else if (row == 3 && bottomRow.size() > 0) {
            bottomRow.remove(resource);
        }
        notifyDepositUpdate(row);
    }

    /**
     * Removes all the given resources from this deposit.
     *
     * @param resources a map that associates resource to quantity
     */
    public synchronized void removeResources(Map<Resource, Integer> resources) {
        for (Resource res : resources.keySet()) {
            int row = findResource(res);
            for (int i = 0; i < resources.get(res); i++) {
                removeResource(row, res);
            }
        }
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
                    row.add(topRow);
                }
                case 2 -> row = middleRow;
                case 3 -> row = bottomRow;
            }
            if (row != null) {
                changes.put(i, row);
            }
        }
        notify(new DepositUpdate(player.getNick(), changes));
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
    public void addMultipleToStrongbox(Map<Resource, Integer> resources){
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
     */
    void removeFromStrongbox(Resource resource) {
        int counter;
        if (strongBox.containsKey(resource) && strongBox.get(resource) > 0) {
            counter = strongBox.get(resource) - 1;
            strongBox.put(resource, counter);
            notifyStrongboxUpdate();
        }
    }

    /**
     * Notifies observers of strongbox update.
     */
    private synchronized void notifyStrongboxUpdate() {
        notify(new DepositStrongboxUpdate(player.getNick(), strongBox));
    }
}
