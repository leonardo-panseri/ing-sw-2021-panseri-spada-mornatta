package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Update sent after a development card has been removed by the deck
 */
public class DevelopmentDeckUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -1459329869594429503L;

    private final List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards;

    /**
     * Constructor: creates a new DevelopmentDeckUpdate
     * @param developmentCards the new deck of development cards
     * */
    public DevelopmentDeckUpdate(List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards) {
        this.developmentCards = developmentCards;
    }

    @Override
    public void process(View view) {
        view.getModel().setDevelopmentDeck(developmentCards);
    }
}
