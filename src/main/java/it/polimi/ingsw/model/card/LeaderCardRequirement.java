package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * Models requirements for leader cards, as they can be resources or development cards.
 */
public class LeaderCardRequirement implements Serializable {
    @Serial
    private static final long serialVersionUID = 4281900239968141563L;

    private final Map<Resource, Integer> resourceRequirements;
    private final Map<CardColor, Integer> cardColorRequirements;
    private final Map<CardColor, Integer> cardLevelRequirements;

    public LeaderCardRequirement(Map<Resource, Integer> resourceRequirements, Map<CardColor, Integer> cardColorRequirements,
                                 Map<CardColor, Integer> cardLevelRequirements) {
        this.resourceRequirements = resourceRequirements;
        this.cardColorRequirements = cardColorRequirements;
        this.cardLevelRequirements = cardLevelRequirements;
    }

    /**
     * Checks if the given Player can afford the card with this requirements.
     *
     * @param player the player that will be checked
     * @return true if the player can afford the card, false otherwise
     */
    boolean canPlayerAfford(Player player) {
        if(!Card.canPlayerAffordResources(player, resourceRequirements))
            return false;
        boolean canAfford = true;
        for(CardColor color : cardColorRequirements.keySet()) {
            int required = cardColorRequirements.get(color);
            int playerAmount = player.getBoard().getAmountOfCardOfColor(color);
            if(playerAmount < required)
                canAfford = false;
        }
        for(CardColor color : cardLevelRequirements.keySet()) {
            int requiredLevel = cardLevelRequirements.get(color);
            if(!player.getBoard().hasCardOfColorAndLevel(color, requiredLevel))
                canAfford = false;
        }
        return canAfford;
    }

    @Override
    public String toString() {
        return "LeaderCardRequirement{" +
                "resourceRequirements=" + resourceRequirements +
                ", cardColorRequirements=" + cardColorRequirements +
                ", cardLevelRequirements=" + cardLevelRequirements +
                '}';
    }
}
