package it.polimi.ingsw.model.card;

import it.polimi.ingsw.constant.DeckParser;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.*;

/**
 * Represent the set of leader cards and the set of development cards,
 * divided in smaller decks as the game specifies.
 */
public class Deck extends Observable<PropertyUpdate> {
    private List<LeaderCard> leaderCards;
    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards;

    /**
     * Constructor for a new Deck. It loads leader cards from a JSON file utilizing the DeckParser.
     * The same occurs for the development cards.
     * Eventually, it organizes the development cards in decks as the game specifies and shuffle them.
     */
    public Deck() {
        leaderCards = DeckParser.loadLeaderCards();
        developmentCards = DeckParser.loadDevelopmentCards();
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : developmentCards){
            for (Stack<DevelopmentCard> stack : map.values()){
                Collections.shuffle(stack);
            }
        }
    }

    /**
     *This method is used in the first phase of the game, when players have to draw 4 leaders each.
     *
     * @return a list of leader cards, the random draw of 4 leaders
     */
    public List<LeaderCard> initialDrawLeaders() {
        List<LeaderCard> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int randomNumber = (int) (Math.random() * (leaderCards.size()));
            while (randomNumber == leaderCards.size()) randomNumber = (int) (Math.random() * (leaderCards.size()));
            result.add(leaderCards.get(randomNumber));
            leaderCards.remove(randomNumber);
        }
        return result;
    }

    /**
     * Removes a card from the Deck when bought.
     *
     * @param card the bought development card to remove.
     */
    public void removeBoughtCard(DevelopmentCard card) {
        developmentCards
                .get(card.getLevel() - 1)
                .get(card.getColor())
                .pop();
    }

}
