package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.util.Map;

/**
 * Subclass that models a DevelopmentCard.
 */
public class DevelopmentCard extends Card {
    @Serial
    private static final long serialVersionUID = -1379804978081533382L;

    private Map<Resource, Integer> cost;
    private int level;
    private Map<Resource, Integer> productionInput;
    private Map<Resource, Integer> productionOutput;
    private CardColor color;

    /**
     * Default constructor for correct GSON deserialization
     */
    public DevelopmentCard() {
        super(-1);
    }

    /**
     * Constructor for a new DevelopmentCard.
     *
     * @param victoryPoints the amount of victory points that this card will give
     * @param cost a map containing the cost of the card, in terms of each resource
     * @param level the level of the card
     * @param productionInput a map of resources needed to activate the production power
     * @param productionOutput a map of resources produced by the production power
     * @param color the color of the card
     */
    public DevelopmentCard(int victoryPoints, Map<Resource, Integer> cost, int level, Map<Resource, Integer> productionInput,
                           Map<Resource, Integer> productionOutput, CardColor color) {
        super(victoryPoints);
        this.cost = cost;
        this.level = level;
        this.productionInput = productionInput;
        this.productionOutput = productionOutput;
        this.color = color;
    }

    @Override
    public boolean canPlayerAfford(Player player) {
        return canPlayerAffordResources(player, cost);
    }

    /**
     * Gets the cost.
     *
     * @return the cost
     */
    public Map<Resource, Integer> getCost() {
        return cost;
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
                "uuid=" + getUuid() +
                ", victoryPoints=" + getVictoryPoints() +
                ", cost=" + cost +
                ", level=" + level +
                ", productionInput=" + productionInput +
                ", productionOutput=" + productionOutput +
                ", color=" + color +
                '}';
    }
}
