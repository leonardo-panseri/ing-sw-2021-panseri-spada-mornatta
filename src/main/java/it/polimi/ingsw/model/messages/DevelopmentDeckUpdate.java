package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class DevelopmentDeckUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -1459329869594429503L;

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

    @Override
    public void process(View view) {

    }
}
