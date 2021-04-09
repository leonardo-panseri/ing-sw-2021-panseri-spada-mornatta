package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Abstract class containing the victory points.
 */

public abstract class Card implements Serializable {
    @Serial
    private static final long serialVersionUID = -5973224638164897402L;

    private UUID uuid;
    private int victoryPoints;

    /**
     * Constructs a Card with a random UUID and the given victory points.
     *
     * @param victoryPoints the amount of victory points of this card
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
     * @return true if the player can afford this card, false otherwise
     */
    public abstract boolean canPlayerAfford(Player player);

    protected static boolean canPlayerAffordResources(Player player, Map<Resource, Integer> resources) {
        boolean canAfford = true;
        for (Resource res : resources.keySet()) {
            int required = resources.get(res);
            int playerAmount = player.getBoard().getDeposit().getAmountOfResource(res);
            if(playerAmount < required)
                canAfford = false;
        }
        return canAfford;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(uuid, card.uuid);
    }
}
