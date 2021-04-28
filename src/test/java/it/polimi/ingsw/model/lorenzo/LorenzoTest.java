package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.lorenzo.action.LorenzoAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoTest {
    Game testGame;
    Lorenzo testLorenzo;
    ArrayList<LorenzoAction> testActions = new ArrayList<>();


    @BeforeEach
    public void initialization() {

        testGame = new Game();
        testLorenzo = testGame.createLorenzo();
        testActions.addAll(LorenzoAction.getAllActions());
    }

    @Test
    public void addPointsTest() {
        assertEquals(0, testLorenzo.getFaithPoints());
        testLorenzo.addPoints(2);
        assertEquals(2, testLorenzo.getFaithPoints());
        testLorenzo.addPoints(10);
        assertEquals(12, testLorenzo.getFaithPoints());
        testLorenzo.addPoints(8);
        assertEquals(20, testLorenzo.getFaithPoints());
        //System.out.println(testLorenzo.getFaithPoints());
    }

    /*@Test
    public void popActionTest() {
        //.out.println(testLorenzo.getActions().toString());
        for (int i = 6; i > 0; i--){
            assertEquals(i,testLorenzo.getActions().size());
             LorenzoAction result = testLorenzo.popAction();
             assertNotNull(result);
        }
    }*/
}