package it.polimi.ingsw.model;

import java.util.List;

/**
 * Represents the base production powers that players can use.
 */
public class BaseProductionPower {
    private List<Resource> input;
    private List<Resource> output;

    public List<Resource> getInput() {
        return input;
    }

    public List<Resource> getOutput() {
        return output;
    }
}
