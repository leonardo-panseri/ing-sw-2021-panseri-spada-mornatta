package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the base production powers that players can use.
 */
public class BaseProductionPower implements Serializable {
    @Serial
    private static final long serialVersionUID = -157626104518121461L;

    private final List<Resource> input;
    private final List<Resource> output;

    /**
     * Constructs a new BaseProductionPower.
     *
     * @param input a list of resources used as input of the base production
     * @param output a list of resources used as output of the base production
     */
    public BaseProductionPower(List<Resource> input, List<Resource> output) {
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
    }

    /**
     * Gets the list of resources used as input of the base production.
     *
     * @return the list of resources used as input of the base production
     */
    public List<Resource> getInput() {
        return new ArrayList<>(input);
    }

    /**
     * Gets the list of resources used as input of the base production as a map.
     *
     * @return a map containing the input resources
     */
    public Map<Resource, Integer> getInputMap() {
        Map<Resource, Integer> input = new HashMap<>();
        for (Resource resource : getInput()) {
            if (input.containsKey(resource))
                input.put(resource, input.get(resource) + 1);
            else
                input.put(resource, 1);
        }
        return input;
    }

    /**
     * Gets the list of resources used as output of the base production.
     *
     * @return the list of resources used as output of the base production
     */
    public List<Resource> getOutput() {
        return new ArrayList<>(output);
    }

    /**
     * Gets the list of resources used as output of the base production as a map.
     *
     * @return a map containing the output resources
     */
    public Map<Resource, Integer> getOutputMap() {
        Map<Resource, Integer> output = new HashMap<>();
        for (Resource resource : getOutput()) {
            if (output.containsKey(resource))
                output.put(resource, output.get(resource) + 1);
            else
                output.put(resource, 1);
        }
        return output;
    }
}
