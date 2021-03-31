package it.polimi.ingsw.view.event;

import it.polimi.ingsw.constant.GsonParser;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class DevelopmentDeckUpdate extends PropertyUpdate {
    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards;

    public DevelopmentDeckUpdate(List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards) {
        this.developmentCards = developmentCards;
    }

    @Override
    public String toString() {
        return "DevelopmentDeckUpdate{" +
                "developmentCards=" + developmentCards +
                '}';
    }
}
