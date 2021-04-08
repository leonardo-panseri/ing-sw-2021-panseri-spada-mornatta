package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.card.DevelopmentCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {

    Player p1;
    PlayerBoard testBoard;
    Deck testDeck;

    @BeforeEach
    void setUp() {
        p1 = new Player("Leo");
        testBoard = p1.getBoard();
        testDeck = new Deck();
    }

    @Test
    void getAmountOfCardOfColor() {

    }

    @Test
    void hasCardOfColorAndLevel() {
    }

    @Test
    void setMarketResults() {
    }
}