package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;

import java.util.Map;

public class DevelopmentCard extends Card {
    private Map<Resource, Integer> cost;
    private int level;
    private Map<Resource, Integer> productionInput;
    private Map<Resource, Integer> productionOutput;
    private CardColor color;

    public DevelopmentCard(Map<Resource, Integer> cost, int level, Map<Resource, Integer> productionInput, Map<Resource, Integer> productionOutput, CardColor color) {
        this.cost = cost;
        this.level = level;
        this.productionInput = productionInput;
        this.productionOutput = productionOutput;
        this.color = color;
    }

    public Map<Resource, Integer> getProductionInput() {
        return productionInput;
    }

    public Map<Resource, Integer> getProductionOutput() {
        return productionOutput;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "DevelopmentCard{" +
                "cost=" + cost +
                ", level=" + level +
                ", productionInput=" + productionInput +
                ", productionOutput=" + productionOutput +
                ", color=" + color +
                '}';
    }
}
