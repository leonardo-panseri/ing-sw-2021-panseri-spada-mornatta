package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;

import java.io.Serial;
import java.io.Serializable;

/**
 * Models a special ability, storing the type and the target resource.
 */

public class SpecialAbility implements Serializable {
    @Serial
    private static final long serialVersionUID = -6196877805986709485L;

    private final SpecialAbilityType type;
    private final Resource targetResource;

    /**
     * Constructor for a new SpecialAbility.
     *
     * @param type a special ability type
     * @param targetResource the target resource
     */
    public SpecialAbility(SpecialAbilityType type, Resource targetResource) {
        this.type = type;
        this.targetResource = targetResource;
    }

    /**
     * Getter for the special ability type.
     *
     * @return a type of special ability
     */
    public SpecialAbilityType getType() {
        return type;
    }

    /**
     * Getter for the target resource.
     *
     * @return a resource
     */
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
