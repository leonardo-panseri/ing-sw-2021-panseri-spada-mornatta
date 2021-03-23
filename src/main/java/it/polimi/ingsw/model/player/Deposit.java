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
    private List<Resource> switchedResources;

    public void addResource(int row, Resource resource) {
        //1 topRow
        //2 middleRow
        //3 bottomRow
        if(middleRow == null){
            middleRow = new ArrayList<>();
        }
        if(bottomRow == null){
            bottomRow = new ArrayList<>();
        }
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
        //1 topRow
        //2 middleRow
        //3 bottomRow
        if (toMove == 1 && destination == 2 && topRow != null) {
            if (middleRow.size() == 1) {
                switchedResources.clear();
                switchedResources = middleRow;
                middleRow.clear();
                middleRow.add(topRow);
                topRow = switchedResources.get(0);
            }
        }
        if (toMove == 1 && destination == 3 && topRow != null) {
            if (bottomRow.size() == 1) {
                switchedResources.clear();
                switchedResources = bottomRow;
                bottomRow.clear();
                bottomRow.add(topRow);
                topRow = switchedResources.get(0);
            }
        }
        if (toMove == 2 && destination == 1 && middleRow != null) {
            if (middleRow.size() == 1) {
                switchedResources.clear();
                switchedResources.add(topRow);
                topRow = middleRow.get(0);
                middleRow.clear();
                middleRow = switchedResources;

            }
        }
        if (toMove == 2 && destination == 3 && middleRow != null) {
            if (middleRow.size() == 2) {
                switchedResources.clear();
                switchedResources = bottomRow;
                bottomRow.clear();
                bottomRow = middleRow;
                middleRow.clear();
                middleRow = switchedResources;
            }
        }
        if (toMove == 3 && destination == 1 && bottomRow != null) {

            if (bottomRow.size() == 1) {
                switchedResources.clear();
                switchedResources.add(topRow);
                topRow = bottomRow.get(0);
                bottomRow.clear();
                bottomRow = switchedResources;
            }
        }
        if (toMove == 3 && destination == 2 && bottomRow != null) {
            if (bottomRow.size() < 3) {
                switchedResources.clear();
                switchedResources = middleRow;
                middleRow = bottomRow;
                bottomRow.clear();
                bottomRow = switchedResources;
            }
        }
    }
}
