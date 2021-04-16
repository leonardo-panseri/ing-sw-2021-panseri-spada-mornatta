package it.polimi.ingsw.model;

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
        testActions.add(LorenzoAction.YELLOW_DEVELOPMENT);
        testActions.add(LorenzoAction.RED_DEVELOPMENT);
        testActions.add(LorenzoAction.GREEN_DEVELOPMENT);
        testActions.add(LorenzoAction.BLUE_DEVELOPMENT);
        testActions.add(LorenzoAction.MOVE);
        testActions.add(LorenzoAction.MOVE_SHUFFLE);
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

    @Test
    public void getNextActionTest() {
        // System.out.println(testActions.toString());
        for (int i = 0; i < 6; i++) {
            LorenzoAction testAction = testLorenzo.getNextAction();
            assert (testActions.contains(testAction));
            // System.out.println(testAction);
            testLorenzo.getActions().remove(testAction);
        }
    }

    @Test
    public void popActionTest() {
        //.out.println(testLorenzo.getActions().toString());
        for (int i = 6; i > 0; i--){
            assertEquals(i,testLorenzo.getActions().size());
            testLorenzo.popAction();
        }
    }

    @Test
    public void shuffleActionsTest(){
        testLorenzo.shuffleActions();
        assertEquals(6,testLorenzo.getActions().size());
        assert(testLorenzo.getActions().contains(LorenzoAction.BLUE_DEVELOPMENT));
        assert(testLorenzo.getActions().contains(LorenzoAction.RED_DEVELOPMENT));
        assert(testLorenzo.getActions().contains(LorenzoAction.GREEN_DEVELOPMENT));
        assert(testLorenzo.getActions().contains(LorenzoAction.YELLOW_DEVELOPMENT));
        assert(testLorenzo.getActions().contains(LorenzoAction.MOVE_SHUFFLE));
        assert(testLorenzo.getActions().contains(LorenzoAction.MOVE));
    }
    
}