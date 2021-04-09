package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Model class representing a box of the {@link Market} containing a marble.
 */
public class Box implements Serializable {
    @Serial
    private static final long serialVersionUID = 6711225986172970079L;

    private Resource resource;

    /**
     * Gets the resource.
     *
     * @return the resource
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets the resource.
     *
     * @param resource the resource to be set
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Box{" +
                "resource=" + resource +
                '}';
    }
}
