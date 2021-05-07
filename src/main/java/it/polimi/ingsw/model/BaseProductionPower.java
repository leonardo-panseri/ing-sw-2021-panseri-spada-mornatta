package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the base production powers that players can use.
 */
public class BaseProductionPower implements Serializable {
    @Serial
    private static final long serialVersionUID = -157626104518121461L;

    private List<Resource> input;
    private List<Resource> output;

    public List<Resource> getInput() {
        return new ArrayList<>(input);
    }

    public List<Resource> getOutput() {
        return new ArrayList<>(output);
    }
}
