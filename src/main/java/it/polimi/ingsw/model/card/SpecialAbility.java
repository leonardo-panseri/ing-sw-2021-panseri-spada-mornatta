package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;

public class SpecialAbility {
    private SpecialAbilityType type;
    private Resource targetResource;

    public SpecialAbility(SpecialAbilityType type, Resource targetResource) {
        this.type = type;
        this.targetResource = targetResource;
    }

    public SpecialAbilityType getType() {
        return type;
    }

    public Resource getTargetResource() {
        return targetResource;
    }

    @Override
    public String toString() {
        return "SpecialAbility{" +
                "type=" + type +
                ", targetResource=" + targetResource +
                '}';
    }
}
