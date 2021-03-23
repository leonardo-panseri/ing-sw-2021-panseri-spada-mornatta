package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarketTest {
    Game testGame;
    Market testMarket;

    @Before
    public void initialization() {
        testGame = new Game();
        testGame.addPlayer(new Player("Davide"));
        testGame.addPlayer(new Player("Leonardo"));
        testGame.addPlayer(new Player("Edoardo"));
        testGame.setCurrentPlayer(testGame.getPlayerAt(0));
        testGame.createMarket();
        testMarket = testGame.getMarket();
    }

    @Test
    public void initializationMarket() {
        for (int i=0; i<3; i++){
            System.out.println(testMarket.getRow(i));
        }
        assertNotNull(testMarket);
    }

    @Test
    public void shiftRowTest() {
        Resource testSlideResource = testMarket.getSlideResource();
        Resource testFirstSphere = testMarket.getRow(0).get(0);
        testMarket.shiftRow(0);
        assertEquals(testMarket.getSlideResource(), testFirstSphere);
        assertEquals(testMarket.getRow(0).get(3), testSlideResource);
    }

}