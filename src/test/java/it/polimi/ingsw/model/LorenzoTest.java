package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoTest {
    Game testGame;
    Lorenzo testLorenzo;
    ArrayList<LorenzoAction> testActions = new ArrayList<>();


    @BeforeEach
    public void initialization() {

        testGame = new Game();
        testLorenzo = testGame.createLorenzo();
        testActions.add(LorenzoAction.YELLOWDEVELOPMENT);
        testActions.add(LorenzoAction.REDDEVELOPMENT);
        testActions.add(LorenzoAction.GREENDEVELOPMENT);
        testActions.add(LorenzoAction.BLUEDEVELOPMENT);
        testActions.add(LorenzoAction.MOVE);
        testActions.add(LorenzoAction.MOVESHUFFLE);
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
        assert(testLorenzo.getActions().contains(LorenzoAction.BLUEDEVELOPMENT));
        assert(testLorenzo.getActions().contains(LorenzoAction.REDDEVELOPMENT));
        assert(testLorenzo.getActions().contains(LorenzoAction.GREENDEVELOPMENT));
        assert(testLorenzo.getActions().contains(LorenzoAction.YELLOWDEVELOPMENT));
        assert(testLorenzo.getActions().contains(LorenzoAction.MOVESHUFFLE));
        assert(testLorenzo.getActions().contains(LorenzoAction.MOVE));
    }
    
}