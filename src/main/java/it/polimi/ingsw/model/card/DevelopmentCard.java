package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;

import java.util.Map;

/**
 * Subclass that models a DevelopmentCard.
 */
public class DevelopmentCard extends Card {
    //private int id;
    private Map<Resource, Integer> cost;
    private int level;
    private Map<Resource, Integer> productionInput;
    private Map<Resource, Integer> productionOutput;
    private CardColor color;

    /**
     * Constructor for a new DevelopmentCard.
     *
     * @param cost a map containing the cost of the card, in terms of each resource
     * @param level the level of the card
     * @param productionInput a map of resources needed to activate the production power
     * @param productionOutput a map of resources produced by the production power
     * @param color the color of the card
     */
    public DevelopmentCard(Map<Resource, Integer> cost, int level, Map<Resource, Integer> productionInput, Map<Resource, Integer> productionOutput, CardColor color) {
        this.cost = cost;
        this.level = level;
        this.productionInput = productionInput;
        this.productionOutput = productionOutput;
        this.color = color;
    }

    /**
     * Returns the pruduction input.
     *
     * @return the production input
     */
    public Map<Resource, Integer> getProductionInput() {
        return productionInput;
    }

    /**
     * Returns the pruduction output.
     *
     * @return the production output
     */
    public Map<Resource, Integer> getProductionOutput() {
        return productionOutput;
    }

    /**
     * Returns the level of the card.
     *
     * @return the level of the card
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the color of the card.
     *
     * @return the color of the card
     */
    public CardColor getColor() {
        return color;
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
