package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.CardsTest;
import it.polimi.ingsw.model.card.DevelopmentCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class PlayerBoardTest {
    private Player player;
    private PlayerBoard testBoard;
    private DevelopmentCard testCard;

    @BeforeEach
    void setUp() {
        player = new Player("Leo");
        testBoard = player.getBoard();
        testCard = CardsTest.getTestDevelopmentCard(4, Map.of(Resource.STONE, 2), 1,
                Map.of(Resource.SHIELD, 1), Map.of(Resource.FAITH, 1), CardColor.GREEN);
    }

    @Test
    void getCardByUuidTest() {
        assertNull(testBoard.getDevelopmentCardByUuid(testCard.getUuid()));
        testBoard.addCard(1, testCard);
        assertNotNull(testBoard.getDevelopmentCardByUuid(testCard.getUuid()));
    }

    @Test
    void getAmountOfCardOfColor() {
        assertEquals(0, testBoard.getAmountOfCardOfColor(CardColor.GREEN));
        testBoard.addCard(2, testCard);
        assertEquals(1, testBoard.getAmountOfCardOfColor(CardColor.GREEN));
        testBoard.addCard(3, testCard);
        assertEquals(2, testBoard.getAmountOfCardOfColor(CardColor.GREEN));
    }

    @Test
    void hasCardOfColorAndLevel() {
        assertFalse(testBoard.hasCardOfColorAndLevel(CardColor.GREEN, 1));
        testBoard.addCard(1, testCard);
        assertTrue(testBoard.hasCardOfColorAndLevel(CardColor.GREEN, 1));
    }

    @Test
    void canPlaceCardOfLevel() {
        assertFalse(testBoard.canPlaceCardOfLevel(1, -1));

        DevelopmentCard cardLvl2 = CardsTest.getTestDevelopmentCard(4, Map.of(Resource.STONE, 2), 2,
                Map.of(Resource.SHIELD, 1), Map.of(Resource.FAITH, 1), CardColor.GREEN);
        DevelopmentCard cardLvl3 = CardsTest.getTestDevelopmentCard(4, Map.of(Resource.STONE, 2), 3,
                Map.of(Resource.SHIELD, 1), Map.of(Resource.FAITH, 1), CardColor.GREEN);
        assertTrue(testBoard.canPlaceCardOfLevel(1, 1));
        assertFalse(testBoard.canPlaceCardOfLevel(2, 1));
        testBoard.addCard(1, testCard);
        assertTrue(testBoard.canPlaceCardOfLevel(2, 1));
        testBoard.addCard(1, cardLvl2);
        assertTrue(testBoard.canPlaceCardOfLevel(3, 1));

        testBoard.addCard(1, cardLvl3);
        assertFalse(testBoard.canPlaceCardOfLevel(4, 1));


    }

    @Test
    void getNumberOfCards() {
        assertEquals(0, testBoard.getNumberOfDevelopmentCards());
        testBoard.addCard(1, testCard);
        assertEquals(1, testBoard.getNumberOfDevelopmentCards());
        testBoard.addCard(2, testCard);
        testBoard.addCard(3, testCard);
        assertEquals(3, testBoard.getNumberOfDevelopmentCards());
    }

    @Test
    void getTotalVictoryPoints() {
        assertEquals(0, testBoard.getDevelopmentCardsTotalVictoryPoints());
        testBoard.addCard(1, testCard);
        assertEquals(4, testBoard.getDevelopmentCardsTotalVictoryPoints());
        testBoard.addCard(2, testCard);
        testBoard.addCard(3, testCard);
        assertEquals(12, testBoard.getDevelopmentCardsTotalVictoryPoints());
    }

    @Test
    void setMarketResults() {
    }
}