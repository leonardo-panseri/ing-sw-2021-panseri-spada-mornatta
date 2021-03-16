package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;

import java.util.Map;

public class DevelopmentCard extends  Card {
    private Map<Resource, Integer> cost;
    private int level;
    private Map<Resource, Integer> productionInput;
    private Map<Resource, Integer> productionOutput;
    private CardColor color;

    public Map<Resource, Integer> getProductionInput() {
        return productionInput;
    }

    public Map<Resource, Integer> getProductionOutput() {
        return productionOutput;
    }
}
