package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepositTest {

    Game testGame;
    Deposit testDeposit;

    void printDepo(){
        System.out.println(testDeposit.getRow(1));
        System.out.println(testDeposit.getRow(2));
        System.out.println(testDeposit.getRow(3) + "\n");
    }

    @BeforeEach
    void setUp() {
        testGame = new Game();
        Player p1 = new Player("Davide");
        testDeposit = new Deposit(p1);
    }

    @Test
    void addResourceTest() {
        testDeposit.addResource(1, Resource.SERVANT);
        testDeposit.addResource(2, Resource.STONE);
        testDeposit.addResource(3, Resource.COIN);

        assertEquals(Resource.SERVANT, testDeposit.getRow(1).get(0));
        assertEquals(Resource.STONE, testDeposit.getRow(2).get(0));
        assertEquals(Resource.COIN, testDeposit.getRow(3).get(0));

        testDeposit.addResource(2, Resource.STONE);
        testDeposit.addResource(3, Resource.COIN);

        assertEquals(Resource.STONE, testDeposit.getRow(2).get(1));
        assertEquals(Resource.COIN, testDeposit.getRow(3).get(1));

        testDeposit.addResource(3, Resource.COIN);

        assertEquals(Resource.COIN, testDeposit.getRow(3).get(2));
    }

    @Test
    void removeResourceTest() {
        addResourceTest();
        testDeposit.removeResource(1, Resource.SERVANT);
        testDeposit.removeResource(2, Resource.STONE);
        testDeposit.removeResource(3, Resource.COIN);

        assertEquals(0, testDeposit.getRow(1).size());
        assertEquals(1, testDeposit.getRow(2).size());
        assertEquals(2, testDeposit.getRow(3).size());
    }

    @Test
    void moveRowTest() {
        removeResourceTest();
        printDepo();
        List<Resource> rowToMove;
        List<Resource> destinationRow;

        rowToMove = new ArrayList<>(testDeposit.getRow(2));
        destinationRow = new ArrayList<>(testDeposit.getRow(3));
        testDeposit.moveRow(2,3);
        printDepo();

        assertEquals(rowToMove, testDeposit.getRow(3));
        assertEquals(destinationRow, testDeposit.getRow(2));

        rowToMove = new ArrayList<>(testDeposit.getRow(3));
        destinationRow = new ArrayList<>(testDeposit.getRow(1));
        testDeposit.moveRow(3,1);
        printDepo();

        assertEquals(rowToMove, testDeposit.getRow(1));
        assertEquals(destinationRow, testDeposit.getRow(3));
    }

    @Test
    void addToStrongboxTest() {
        testDeposit.addToStrongbox(Resource.SHIELD);
        testDeposit.addToStrongbox(Resource.COIN);
        testDeposit.addToStrongbox(Resource.COIN);
        testDeposit.addToStrongbox(Resource.SERVANT);

        assertEquals(1, testDeposit.getStrongBox().get(Resource.SHIELD));
        assertEquals(2, testDeposit.getStrongBox().get(Resource.COIN));
        assertEquals(1, testDeposit.getStrongBox().get(Resource.SERVANT));
    }

    @Test
    void removeFromStrongbox() {
        addToStrongboxTest();
        testDeposit.removeFromStrongbox(Resource.SHIELD);
        testDeposit.removeFromStrongbox(Resource.COIN);
        testDeposit.removeFromStrongbox(Resource.COIN);
        testDeposit.removeFromStrongbox(Resource.SERVANT);

        assertEquals(0, testDeposit.getStrongBox().get(Resource.SHIELD));
        assertEquals(0, testDeposit.getStrongBox().get(Resource.COIN));
        assertEquals(0, testDeposit.getStrongBox().get(Resource.SERVANT));
    }
}