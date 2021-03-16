package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;

public class SpecialAbility {
    private SpecialAbilityType type;
    private Resource targetResource;

    public SpecialAbilityType getType() {
        return type;
    }

    public Resource getTargetResource() {
        return targetResource;
    }
}
