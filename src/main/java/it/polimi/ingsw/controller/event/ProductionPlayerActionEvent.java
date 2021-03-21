package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;

import java.util.List;

public class ProductionPlayerActionEvent extends PlayerActionEvent {
    private Card card;
    private List<Resource> result;
    private Resource[] baseProductionInput;
    private Resource baseProductionOutput;
}
