package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.lorenzo.action.DevelopmentAction;
import it.polimi.ingsw.model.lorenzo.action.LorenzoAction;
import it.polimi.ingsw.model.messages.LorenzoActionUpdate;
import it.polimi.ingsw.model.messages.TurnUpdate;
import it.polimi.ingsw.server.IServerPacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoTest {
    private Lorenzo testLorenzo;

    @BeforeEach
    public void initialization() {
        testLorenzo = new Game().createLorenzo();
    }

    @Test
    void addPointsTest() {
        assertEquals(0, testLorenzo.getFaithPoints());
        testLorenzo.addPoints(2);
        assertEquals(2, testLorenzo.getFaithPoints());
        testLorenzo.addPoints(10);
        assertEquals(12, testLorenzo.getFaithPoints());
        testLorenzo.addPoints(8);
        assertEquals(20, testLorenzo.getFaithPoints());
    }

    @Test
    void popActionTest() {
        for (int i = 6; i > 0; i--){
            assertEquals(i, testLorenzo.getActions().size());
            LorenzoAction result = testLorenzo.popAction();
            assertNotNull(result);
        }
        assertNull(testLorenzo.popAction());
    }

    @Test
    void endTurnTest() {
        List<IServerPacket> packets = new ArrayList<>();
        testLorenzo.addObserver(packets::add);
        testLorenzo.endTurn("Test", new DevelopmentAction(CardColor.PURPLE));
        assertTrue(packets.get(0) instanceof LorenzoActionUpdate);
        assertTrue(packets.get(1) instanceof TurnUpdate);
    }
}