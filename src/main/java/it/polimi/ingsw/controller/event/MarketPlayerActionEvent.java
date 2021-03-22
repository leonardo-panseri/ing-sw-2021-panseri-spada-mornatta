package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.model.Resource;

import java.util.List;

public class MarketPlayerActionEvent extends PlayerActionEvent {
    private int selected;
    private Resource whiteConversion;
    private List<Resource> result;

    public int getSelected() {
        return selected;
    }

    public Resource getWhiteConversion() {
        return whiteConversion;
    }

    public List<Resource> getResult() {
        return result;
    }

    public void setResult(List<Resource> result) {
        this.result = result;
    }
}
