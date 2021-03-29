package it.polimi.ingsw.model.card;

import java.util.Map;

/**
 * Subclass that models a leader card.
 */
public class LeaderCard extends Card {
    private Map<LeaderCardRequirement, Integer> cardRequirements;
    private SpecialAbility specialAbility;

    /**
     * Constructor for a new LeaderCard.
     *
     * @param cardRequirements a map that stores the requirements, resources or cards, needed to activate the leader.
     * @param specialAbility the special ability granted by the card upon activation.
     */
    public LeaderCard(Map<LeaderCardRequirement, Integer> cardRequirements, SpecialAbility specialAbility) {
        this.cardRequirements = cardRequirements;
        this.specialAbility = specialAbility;
    }

    /**
     * Getter for the requirements.
     *
     * @return the card requirements.
     */
    public Map<LeaderCardRequirement, Integer> getCardRequirements() {
        return cardRequirements;
    }

    /**
     * Getter for the special ability.
     *
     * @return the special ability
     */
    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    @Override
    public String toString() {
        return "LeaderCard{" +
                "cardRequirements=" + cardRequirements +
                ", specialAbility=" + specialAbility +
                '}';
    }
}
