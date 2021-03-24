package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Deposit {
    private Resource topRow;
    private List<Resource> middleRow;
    private List<Resource> bottomRow;
    private Map<Resource, Integer> strongBox;

    public Deposit() {
        middleRow = new ArrayList<>();
        bottomRow = new ArrayList<>();
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
                    if (middleRow.size() == 1) {
                        // Top <--> Middle
                        switchedResources = middleRow;
                        middleRow.clear();
                        middleRow.add(topRow);
                        topRow = switchedResources.get(0);
                    } else if (bottomRow.size() == 1) {
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
                    if (middleRow.size() == 1) {
                        // Middle <--> Top
                        switchedResources.add(topRow);
                        topRow = middleRow.get(0);
                        middleRow.clear();
                        middleRow = switchedResources;
                    } else if (middleRow.size() == 2) {
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
                    if (bottomRow.size() == 1) {
                        // Bottom <--> Top
                        switchedResources.add(topRow);
                        topRow = bottomRow.get(0);
                        bottomRow.clear();
                        bottomRow = switchedResources;
                    } else if (bottomRow.size() == 2) {
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

}
