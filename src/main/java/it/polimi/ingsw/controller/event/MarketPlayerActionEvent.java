package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.model.Resource;

import java.util.List;

public class MarketPlayerActionEvent extends PlayerActionEvent {
    private int selected;
    private Resource whiteConversion;
    private List<Resource> result;
}
