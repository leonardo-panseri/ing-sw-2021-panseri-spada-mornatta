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
        for (int i = 0; i < 3; i++) {
            System.out.println(testMarket.getRow(i));
        }
        System.out.println("\n");
    }

    @Test
    public void initializationMarket() {

        int coinCounter = 0;
        int stoneCounter = 0;
        int shieldCounter = 0;
        int servantCounter = 0;
        int faithCounter = 0;
        int blankCounter = 0;
        for (int i = 0; i < 4; i++) {
            for (Resource res : testMarket.getColumn(i)) {
                if (res == null) blankCounter++;
                else {
                    switch (res) {
                        case COIN:
                            coinCounter++;
                            break;
                        case STONE:
                            stoneCounter++;
                            break;
                        case SHIELD:
                            shieldCounter++;
                            break;
                        case SERVANT:
                            servantCounter++;
                            break;
                        case FAITH:
                            faithCounter++;
                            break;
                    }
                }
            }
        }
        if (testMarket.getSlideResource() == null) blankCounter++;
        else {
            switch (testMarket.getSlideResource()) {
                case COIN:
                    coinCounter++;
                    break;
                case STONE:
                    stoneCounter++;
                    break;
                case SHIELD:
                    shieldCounter++;
                    break;
                case SERVANT:
                    servantCounter++;
                    break;
                case FAITH:
                    faithCounter++;
                    break;
            }
        }

        assertEquals(2, coinCounter);
        assertEquals(2, stoneCounter);
        assertEquals(2, shieldCounter);
        assertEquals(2, servantCounter);
        assertEquals(1, faithCounter);
        assertEquals(4, blankCounter);
    }

    @Test
    public void shiftRowTest() {
        Resource testSlideResource = testMarket.getSlideResource();
        Resource testFirstSphere = testMarket.getRow(0).get(0);
        testMarket.shiftRow(0);

        for (int i = 0; i < 3; i++) {
            System.out.println(testMarket.getRow(i));
        }

        assertEquals(testMarket.getSlideResource(), testFirstSphere);
        assertEquals(testMarket.getRow(0).get(3), testSlideResource);
    }

    @Test
    public void shiftColumnTest() {
        Resource testSlideResource = testMarket.getSlideResource();
        Resource testFirstSphere = testMarket.getColumn(0).get(0);
        testMarket.shiftColumn(0);

        for (int i = 0; i < 3; i++) {
            System.out.println(testMarket.getRow(i));
        }

        assertEquals(testMarket.getSlideResource(), testFirstSphere);
        assertEquals(testMarket.getColumn(0).get(2), testSlideResource);
    }

}