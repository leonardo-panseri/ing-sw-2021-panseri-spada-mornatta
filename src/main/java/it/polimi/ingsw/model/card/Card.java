package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

import java.util.UUID;

/**
 * Abstract class containing the victory points.
 */

public abstract class Card {
    private UUID uuid;
    private int victoryPoints;

    /**
     * Constructs a Card with a random UUID and the given victory points.
     *
     * @param victoryPoints
     */
    public Card(int victoryPoints) {
        this.uuid = UUID.randomUUID();
        this.victoryPoints = victoryPoints;
    }

    /**
     * Gets the UUID of this cars.
     *
     * @return the uuid of this card
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Getter for the victory points
     *
     * @return the victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Checks if the given Player can afford this card.
     *
     * @param player the player that will be checked
     * @return true if the player can afford this cards, false otherwise
     */
    public abstract boolean canPlayerAfford(Player player);
}
