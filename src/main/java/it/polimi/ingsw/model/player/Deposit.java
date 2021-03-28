package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deposit extends Observable<PropertyUpdate> {
    private Player player;
    private Resource topRow;
    private List<Resource> middleRow;
    private List<Resource> bottomRow;
    private Map<Resource, Integer> strongBox;

    public Deposit(Player player) {
        middleRow = new ArrayList<>();
        bottomRow = new ArrayList<>();
        strongBox = new HashMap<>();
        this.player = player;
    }

    public void addResource(int row, Resource resource) {
        //1 topRow
        //2 middleRow
        //3 bottomRow


        if (row == 1 && topRow == null) {
            topRow = resource;
        } else if (row == 2 && middleRow.size() < 2) {
            middleRow.add(resource);
        } else if (row == 3 && bottomRow.size() < 3) {
            bottomRow.add(resource);
        }

    }

    public void removeResource(int row, Resource resource) {
        if (row == 1 && topRow != null) {
            topRow = null;
        } else if (row == 2 && middleRow.size() > 0) {
            middleRow.remove(resource);
        } else if (row == 3 && bottomRow.size() > 0) {
            bottomRow.remove(resource);
        }
    }

    public void moveRow(int toMove, int destination) {
        List<Resource> switchedResources = new ArrayList<>();
        switch (toMove) {
            case 1:
                if (topRow != null) {
                    if (middleRow.size() == 1 && destination == 2) {
                        // Top <--> Middle
                        switchedResources = middleRow;
                        middleRow.clear();
                        middleRow.add(topRow);
                        topRow = switchedResources.get(0);
                    } else if (bottomRow.size() == 1 && destination == 3) {
                        // Top <--> Bottom
                        switchedResources = bottomRow;
                        bottomRow.clear();
                        bottomRow.add(topRow);
                        topRow = switchedResources.get(0);
                    }
                }
                break;

            case 2:
                if (middleRow != null) {
                    if (middleRow.size() == 1 && destination == 1) {
                        // Middle <--> Top
                        switchedResources.add(topRow);
                        topRow = middleRow.get(0);
                        middleRow.clear();
                        middleRow = switchedResources;
                    } else if (middleRow.size() == 2 && destination == 3)   {
                        // Middle <--> Bottom
                        switchedResources = bottomRow;
                        bottomRow.clear();
                        bottomRow = middleRow;
                        middleRow.clear();
                        middleRow = switchedResources;
                    }
                }
                break;

            case 3:
                if (bottomRow != null) {
                    if (bottomRow.size() == 1 && destination == 1) {
                        // Bottom <--> Top
                        switchedResources.add(topRow);
                        topRow = bottomRow.get(0);
                        bottomRow.clear();
                        bottomRow = switchedResources;
                    } else if (bottomRow.size() == 2 && destination == 2) {
                        // Bottom <--> Middle
                        switchedResources = middleRow;
                        middleRow = bottomRow;
                        bottomRow.clear();
                        bottomRow = switchedResources;
                    }
                }
                break;
        }
    }

    public void addToStrongbox(Resource resource) {
        int counter = 0;
        if (strongBox.containsKey(resource)) {
            counter = strongBox.get(resource) + 1;
        }
        strongBox.put(resource, counter);
    }

    public void removeFromStrongbox(Resource resource) {
        int counter = 0;
        if (strongBox.containsKey(resource) && strongBox.get(resource) > 0) {
            counter = strongBox.get(resource) - 1;
            strongBox.put(resource, counter);
        }
    }
}
