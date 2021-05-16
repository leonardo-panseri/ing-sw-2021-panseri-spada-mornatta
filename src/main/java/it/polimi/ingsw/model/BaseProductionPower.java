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

    private List<Resource> input;
    private List<Resource> output;

    public BaseProductionPower(List<Resource> input, List<Resource> output) {
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
    }

    public List<Resource> getInput() {
        return new ArrayList<>(input);
    }

    public Map<Resource, Integer> getInputMap() {
        Map<Resource, Integer> input = new HashMap<>();
        for(Resource resource : getInput()) {
            if(input.containsKey(resource))
                input.put(resource, input.get(resource) + 1);
            else
                input.put(resource, 1);
        }
        return input;
    }

    public List<Resource> getOutput() {
        return new ArrayList<>(output);
    }

    public Map<Resource, Integer> getOutputMap() {
        Map<Resource, Integer> output = new HashMap<>();
        for(Resource resource : getOutput()) {
            if(output.containsKey(resource))
                output.put(resource, output.get(resource) + 1);
            else
                output.put(resource, 1);
        }
        return output;
    }
}
