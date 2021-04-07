package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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

    @Test
    public void removeAllTest() {
        Deck testDeck2 = new Deck();

        for (HashMap<CardColor, Stack<DevelopmentCard>> map : testDeck2.getDevelopmentCards()) {
            for (Stack<DevelopmentCard> stack : map.values()) {
                for (DevelopmentCard card : stack) {
                    testDeck.removeBoughtCard(card);
                }
            }
        }

        for (HashMap<CardColor, Stack<DevelopmentCard>> map : testDeck.getDevelopmentCards()) {
            for (Stack<DevelopmentCard> stack : map.values()) {
                assertEquals(0, stack.size());
            }
        }
    }

}