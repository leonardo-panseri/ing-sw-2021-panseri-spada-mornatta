package it.polimi.ingsw.model;

/**
 * Model class representing a box of the {@link Market} containing a marble.
 */
public class Box {
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
}
