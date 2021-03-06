package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    private static Deck testDeck;

    public static DevelopmentCard getCardByLevel(int level, Deck deck) {
        return deck.getDevelopmentCards().get(level-1).get(CardColor.GREEN).peek();
    }

    @BeforeEach
    public void init() {
        testDeck = new Game().getDeck();
    }

    @Test
    void getDevelopmentCardByUuidTest() {
        DevelopmentCard cardGot;
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : testDeck.getDevelopmentCards()) {
            for (Stack<DevelopmentCard> stack : map.values()) {
                for(DevelopmentCard card : stack) {
                    cardGot = testDeck.getDevelopmentCardByUuid(card.getUuid());
                    assertEquals(cardGot.getColor(), card.getColor());
                    assertEquals(cardGot.getUuid(), card.getUuid());
                    assertEquals(cardGot.getLevel(), card.getLevel());
                    assertEquals(cardGot.getCost(), card.getCost());
                    assertEquals(cardGot.getVictoryPoints(), card.getVictoryPoints());
                    assertEquals(cardGot.getProductionInput(), card.getProductionInput());
                    assertEquals(cardGot.getProductionOutput(), card.getProductionOutput());
                }
            }
        }
    }

    @Test
    void shuffleDevelopmentCardTest() {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCardsBefore = developmentCardsDeepCopy(testDeck.getDevelopmentCards());
        testDeck.shuffleDevelopmentDeck();
        for(int i = 0; i < testDeck.getDevelopmentCards().size(); i++) {
            HashMap<CardColor, Stack<DevelopmentCard>> map = testDeck.getDevelopmentCards().get(i);
            int finalI = i;
            map.forEach((color, stack) -> developmentCardsBefore.get(finalI).get(color).forEach(card -> assertTrue(stack.contains(card))));
        }
    }

    @Test
    void initialDrawTest() {
        List<LeaderCard> draw = testDeck.initialDrawLeaders();
        assertEquals(4, draw.size());
    }

    @Test
    void removeCardTest() {
        Random random = new Random();
        Stack<DevelopmentCard> randomStack = testDeck.getDevelopmentCards()
                .get(random.nextInt(3))
                .get(CardColor.values()[random.nextInt(4)]);
        DevelopmentCard randomCard = randomStack.peek();
        assertTrue(randomStack.contains(randomCard));
        assertNotEquals(null, testDeck.getDevelopmentCardByUuid(randomCard.getUuid()));

        testDeck.removeBoughtCard(randomCard);
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : testDeck.getDevelopmentCards()) {
            for (Stack<DevelopmentCard> stack : map.values()) {
                assertFalse(stack.contains(randomCard));
            }
        }
    }

    @Test
    void removeTwoCardsOfSameColorTest() {
        List<DevelopmentCard> cardsBefore;
        List<DevelopmentCard> cardsAfter;
        for(CardColor color : CardColor.values()) {
            cardsBefore = getAllCardsOfColor(testDeck.getDevelopmentCards(), color);
            testDeck.removeTwoDevelopmentCards(color);
            cardsAfter = getAllCardsOfColor(testDeck.getDevelopmentCards(), color);

            int notFound = 0;
            for(DevelopmentCard card : cardsBefore) {
                if(!cardsAfter.contains(card))
                    notFound++;
            }
            assertEquals(2, notFound);
        }
    }

    @Test
    void isColorEmptyTest() {
        for(HashMap<CardColor, Stack<DevelopmentCard>> map : testDeck.getDevelopmentCards()) {
            for(CardColor color : map.keySet())
                assertFalse(testDeck.isColorEmpty(color));

            for(CardColor color : map.keySet()) {
                Iterator<DevelopmentCard> iterator = map.get(color).iterator();
                while(iterator.hasNext()) {
                    iterator.next();
                    iterator.remove();
                }
            }
        }

        for(HashMap<CardColor, Stack<DevelopmentCard>> map : testDeck.getDevelopmentCards()) {
            for(CardColor color : map.keySet())
                assertTrue(testDeck.isColorEmpty(color));
        }
    }

    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCardsDeepCopy(List<HashMap<CardColor, Stack<DevelopmentCard>>> input) {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> output = new ArrayList<>();
        input.forEach(map -> {
            HashMap<CardColor, Stack<DevelopmentCard>> newMap = new HashMap<>();
            map.forEach((color, stack) -> {
                Stack<DevelopmentCard> newStack = new Stack<>();
                newStack.addAll(stack);
                newMap.put(color, newStack);
            });
            output.add(newMap);
        });
        return output;
    }

    private List<DevelopmentCard> getAllCardsOfColor(List<HashMap<CardColor, Stack<DevelopmentCard>>> input, CardColor colorToSearch) {
        List<DevelopmentCard> result = new ArrayList<>();
        input.forEach(map -> map.forEach((color, stack) -> {if(color == colorToSearch) result.addAll(stack);}));
        return result;
    }
}