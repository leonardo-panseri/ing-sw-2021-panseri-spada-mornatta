package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;

import java.util.Map;

public class LeaderCard {
    private Map<CardColor, Integer> cardRequirements;
    private Map<Resource, Integer> resourceRequirements;
    private SpecialAbility specialAbility;

    public LeaderCard(Map<CardColor, Integer> cardRequirements, Map<Resource, Integer> resourceRequirements, SpecialAbility specialAbility) {
        this.cardRequirements = cardRequirements;
        this.resourceRequirements = resourceRequirements;
        this.specialAbility = specialAbility;
    }

    public Map<CardColor, Integer> getCardRequirements() {
        return cardRequirements;
    }

    public Map<Resource, Integer> getResourceRequirements() {
        return resourceRequirements;
    }

    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    @Override
    public String toString() {
        return "LeaderCard{" +
                "cardRequirements=" + cardRequirements +
                ", resourceRequirements=" + resourceRequirements +
                ", specialAbility=" + specialAbility +
                '}';
    }
}
