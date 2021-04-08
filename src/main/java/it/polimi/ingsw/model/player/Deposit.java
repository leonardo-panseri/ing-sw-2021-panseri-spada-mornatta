package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.DepositStrongboxUpdate;
import it.polimi.ingsw.view.event.DepositUpdate;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models the deposit of a player board, comprehensive of the three rows of resources slots, adn the strongbox.
 * Rows are indexed starting from 1 and ending to 3, from top to bottom.
 */
public class Deposit extends Observable<PropertyUpdate> {
    private Player player;
    private Resource topRow;
    private List<Resource> middleRow;
    private List<Resource> bottomRow;
    private Map<Resource, Integer> strongBox;

    /**
     * Getter: gets the requested row of resources.
     *
     * @param row the index of the wanted row of resources
     * @return a list with the resources
     */
    List<Resource> getRow(int row) {
        if (row == 1) {
            List<Resource> result = new ArrayList<>();
            if(topRow != null) result.add(topRow);
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
    public Deposit(Player player) {
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
    public int getAmountOfResource(Resource resource) {
        int amount = 0;
        if(topRow == resource)
            amount++;
        for(Resource res : middleRow)
            if(res == resource)
                amount++;
        for(Resource res : bottomRow)
            if(res == resource)
                amount++;
        amount += strongBox.getOrDefault(resource, 0);
        return amount;
    }

    /**
     * Adds the desired resource into a free slot on the indicated row, if the move if possible.
     *
     * @param row      the row where to insert the resource in, 1 is the top row, 2 is the middle row and 3 is the bottom row
     * @param resource the resource to insert
     */
    public void addResource(int row, Resource resource) {
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
    public void removeResource(int row, Resource resource) {
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
    public void removeResources(Map<Resource, Integer> resources) {
        //TODO Write methods
    }

    /**
     * Switches two rows of resources, checking if the move is possible.
     *
     * @param toMove      row to move
     * @param destination row to be switched with the previous one
     */
    public void moveRow(int toMove, int destination) {
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
    public void addToStrongbox(Resource resource) {
        int counter = 1;
        if (strongBox.containsKey(resource)) {
            counter = strongBox.get(resource) + 1;
        }
        strongBox.put(resource, counter);
        notifyStrongboxUpdate();
    }

    /**
     * Removes the desired resource into the strongbox, modeled by a map of resources and integers.
     * If the type of resource is stored, it removes one to the current related counter.
     *
     * @param resource the resource to be removed
     */
    public void removeFromStrongbox(Resource resource) {
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
    private void notifyStrongboxUpdate() {
        notify(new DepositStrongboxUpdate(player.getNick(), strongBox));
    }
}
