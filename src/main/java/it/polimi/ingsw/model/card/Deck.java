package it.polimi.ingsw.model.card;

import it.polimi.ingsw.constant.DeckParser;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.model.messages.DevelopmentDeckUpdate;
import it.polimi.ingsw.model.messages.PropertyUpdate;
import it.polimi.ingsw.server.IServerPacket;

import java.util.*;

/**
 * Represent the set of leader cards and the set of development cards,
 * divided in smaller decks as the game specifies.
 */
public class Deck extends Observable<IServerPacket> {
    private List<LeaderCard> leaderCards;
    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards;

    /**
     * Constructs a new Deck. It loads leader cards from a JSON file utilizing the DeckParser.
     * The same occurs for the development cards.
     */
    public Deck() {
        leaderCards = DeckParser.loadLeaderCards();
        developmentCards = DeckParser.loadDevelopmentCards();
    }

    //TODO Getting the card by uuid can return null because the cards get removed from the deck

    /**
     * Gets the LeaderCard with the given UUID.
     *
     * @param uuid the uuid of the card to search for
     * @return the leader card with the given UUID or <code>null</code> if not found
     */
    public LeaderCard getLeaderCardByUuid(UUID uuid) {
        LeaderCard result = null;
        for(LeaderCard card : leaderCards) {
            if(uuid.equals(card.getUuid())) {
                result = card;
                break;
            }
        }
        return result;
    }

    /**
     * Gets the DevelopmentCard with the given UUID.
     *
     * @param uuid the uuid of the card to search for
     * @return the development card with the given UUID or <code>null</code> if not found
     */
    public DevelopmentCard getDevelopmentCardByUuid(UUID uuid) {
        DevelopmentCard result = null;
        outerLoop:
        for(HashMap<CardColor, Stack<DevelopmentCard>> color : developmentCards) {
            for(Stack<DevelopmentCard> pile : color.values()) {
                for(DevelopmentCard card : pile) {
                    if(uuid.equals(card.getUuid())) {
                        result = card;
                        break outerLoop;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Gets the development cards.
     *
     * @return the development cards
     */
    public List<HashMap<CardColor, Stack<DevelopmentCard>>> getDevelopmentCards() {
        return developmentCards;
    }

    /**
     * Shuffles the development cards.
     */
    public void shuffleDevelopmentDeck() {
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : developmentCards){
            for (Stack<DevelopmentCard> stack : map.values()){
                Collections.shuffle(stack);
            }
        }
        notify(new DevelopmentDeckUpdate(developmentCards));
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
