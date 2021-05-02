package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    Game testGame;
    Deck testDeck;
    DevelopmentCard devCard;
    Map<Resource, Integer> reqMap;
    int level;
    Map<Resource, Integer> inMap;
    Map<Resource, Integer> outMap;
    CardColor color;


    @BeforeEach
    public void init() {
        testGame = new Game();
        testDeck = testGame.getDeck();

        reqMap = new HashMap<>();
        reqMap.put(Resource.SERVANT, 3);

        level = 1;

        inMap = new HashMap<>();
        inMap.put(Resource.COIN, 1);

        outMap = new HashMap<>();
        outMap.put(Resource.STONE, 3);

        color = CardColor.GREEN;

        devCard = new DevelopmentCard(1, reqMap, level, inMap, outMap, color);
    }

    @Test
    public void getDevelopmentCardByUuidTest() {
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
    public void shuffleDevelopmentCardTest() {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCardsBefore = developmentCardsDeepCopy(testDeck.getDevelopmentCards());
        testDeck.shuffleDevelopmentDeck();
        for(int i = 0; i < testDeck.getDevelopmentCards().size(); i++) {
            HashMap<CardColor, Stack<DevelopmentCard>> map = testDeck.getDevelopmentCards().get(i);
            int finalI = i;
            map.forEach((color, stack) -> {
                developmentCardsBefore.get(finalI).get(color).forEach(card -> assertTrue(stack.contains(card)));
            });
        }
    }

    @Test
    public void initialDrawTest() {
        List<LeaderCard> draw = testDeck.initialDrawLeaders();
        assertEquals(4, draw.size());
    }

    @Test
    public void removeCardTest() {
        testDeck.removeBoughtCard(devCard);
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : testDeck.getDevelopmentCards()) {
            for (Stack<DevelopmentCard> stack : map.values()) {
                assertFalse(stack.contains(devCard));
            }
        }
    }

    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCardsDeepCopy(List<HashMap<CardColor, Stack<DevelopmentCard>>> input) {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> output = new ArrayList<>();
        testDeck.getDevelopmentCards().forEach(map -> {
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
}