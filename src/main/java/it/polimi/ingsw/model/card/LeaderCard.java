package it.polimi.ingsw.model.card;

import java.util.Map;

public class LeaderCard extends Card {
    private Map<LeaderCardRequirement, Integer> cardRequirements;
    private SpecialAbility specialAbility;

    public LeaderCard(Map<LeaderCardRequirement, Integer> cardRequirements, SpecialAbility specialAbility) {
        this.cardRequirements = cardRequirements;
        this.specialAbility = specialAbility;
    }

    public Map<LeaderCardRequirement, Integer> getCardRequirements() {
        return cardRequirements;
    }

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
