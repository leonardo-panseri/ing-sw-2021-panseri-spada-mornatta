package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;

import java.util.Map;

public class LeaderCard {
    private Map<CardColor, Integer> cardRequirements;
    private Map<Resource, Integer> resourceRequirements;
    private SpecialAbility specialAbility;

    public Map<CardColor, Integer> getCardRequirements() {
        return cardRequirements;
    }

    public Map<Resource, Integer> getResourceRequirements() {
        return resourceRequirements;
    }

    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }
}
