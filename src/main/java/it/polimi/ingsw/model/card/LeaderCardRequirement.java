package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.Map;

/**
 * Models requirements for leader cards, as they can be resources or development cards.
 */
public class LeaderCardRequirement {
    private final Map<Resource, Integer> resourceRequirements;
    private final Map<CardColor, Integer> cardColorRequirements;
    private final Map<CardColor, Integer> cardLevelRequirements;

    public LeaderCardRequirement(Map<Resource, Integer> resourceRequirements, Map<CardColor, Integer> cardColorRequirements,
                                 Map<CardColor, Integer> cardLevelRequirements) {
        this.resourceRequirements = resourceRequirements;
        this.cardColorRequirements = cardColorRequirements;
        this.cardLevelRequirements = cardLevelRequirements;
    }

    boolean canPlayerAfford(Player player) {
        //TODO Scrivere metodo
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
