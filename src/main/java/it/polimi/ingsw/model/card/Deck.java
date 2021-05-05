package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.messages.DevelopmentDeckUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.server.IServerPacket;

import java.util.*;

/**
 * Represent the set of leader cards and the set of development cards,
 * divided in smaller decks as the game specifies.
 */
public class Deck extends Observable<IServerPacket> {
    private final List<LeaderCard> leaderCards;
    private final List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards;

    /**
     * Constructs a new Deck, loading cards from the default game configurations.
     */
    public Deck() {
        GameConfig gameConfig = GameConfig.loadDefaultGameConfig();
        if (gameConfig == null) {
            System.err.println("GameConfig is null in Deck constructor");
            leaderCards = null;
            developmentCards = null;
            return;
        }
        leaderCards = gameConfig.getLeaderCards();
        developmentCards = gameConfig.getDevelopmentCards();
    }

    /**
     * Constructs a new Deck with the given cards.
     *
     * @param leaderCards the leader cards
     * @param developmentCards the development cards
     */
    public Deck(List<LeaderCard> leaderCards, List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards) {
        this.leaderCards = leaderCards;
        this.developmentCards = developmentCards;
    }

    /**
     * Gets the DevelopmentCard with the given UUID.
     *
     * @param uuid the uuid of the card to search for
     * @return the development card with the given UUID or <code>null</code> if not found
     */
    public synchronized DevelopmentCard getDevelopmentCardByUuid(UUID uuid) {
        DevelopmentCard result = null;
        outerLoop:
        for (HashMap<CardColor, Stack<DevelopmentCard>> color : developmentCards) {
            for (Stack<DevelopmentCard> pile : color.values()) {
                for (DevelopmentCard card : pile) {
                    if (uuid.equals(card.getUuid())) {
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
    List<HashMap<CardColor, Stack<DevelopmentCard>>> getDevelopmentCards() {
        return developmentCards;
    }

    /**
     * Shuffles the development cards.
     */
    public void shuffleDevelopmentDeck() {
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : developmentCards) {
            for (Stack<DevelopmentCard> stack : map.values()) {
                Collections.shuffle(stack);
            }
        }
        notify(new DevelopmentDeckUpdate(developmentCards));
    }

    /**
     * This method is used in the first phase of the game, when players have to draw 4 leaders each.
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
     * @throws EmptyStackException if the given card is not in the deck
     */
    public synchronized void removeBoughtCard(DevelopmentCard card) throws EmptyStackException {
        DevelopmentCard removedCard = developmentCards.get(card.getLevel() - 1).get(card.getColor()).pop();
        if(!removedCard.getUuid().equals(card.getUuid()))
            System.err.println("Removed a card with the wrong UUID");
    }

    /**
     * Removes two DevelopmentCards of the lowest level from the deck.
     *
     * @param color the color of the cards that will be removed
     */
    public void removeTwoDevelopmentCards(CardColor color) {
        int removed = 0;
        int index = 0;
        while(removed < 2 && index < 3) {
            if (!developmentCards.get(index).get(color).isEmpty()) {
                developmentCards.get(index).get(color).pop();
                removed++;
            } else
                index++;
        }
        if(removed > 0)
            notify(new DevelopmentDeckUpdate(developmentCards));
    }

    /**
     * Checks if there are no DevelopmentCards of this color left in the Deck.
     *
     * @param color the color of the cards to search for
     * @return true if there are no more cards of this color
     */
    public boolean isColorEmpty(CardColor color) {
        boolean empty = true;
        for(HashMap<CardColor, Stack<DevelopmentCard>> pile : developmentCards) {
            if(!pile.get(color).isEmpty()) empty = false;
        }
        return empty;
    }
}
