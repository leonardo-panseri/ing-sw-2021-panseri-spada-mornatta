package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.lorenzo.action.DevelopmentAction;
import it.polimi.ingsw.model.lorenzo.action.LorenzoAction;
import it.polimi.ingsw.model.lorenzo.action.MoveAction;
import it.polimi.ingsw.model.lorenzo.action.MoveShuffleAction;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoControllerTest {

    Lobby lobby;
    GameController gc;
    LorenzoController lorenzoController;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        lobby = new Lobby();
        Field pNum = lobby.getClass().getDeclaredField("playersToStart");
        pNum.setAccessible(true);
        pNum.set(lobby, 1);

        gc = new GameController(lobby);
        lorenzoController = gc.getLorenzoController();
        gc.getGame().createLorenzo();
    }

    @Test
    void executeSpecificActionsTest() {
        LorenzoAction a = new DevelopmentAction(CardColor.GREEN);
        LorenzoAction b = new MoveAction();
        LorenzoAction c = new MoveShuffleAction();

        //Discard development cards, but there are still some of the same color
        assertFalse(a.execute(lorenzoController));

        //Discard development cards of the same color until the end of them
        while (!gc.getGame().getDeck().isColorEmpty(CardColor.GREEN)) {
            a.execute(lorenzoController);
        }
        assertEquals(GamePhase.END, gc.getGame().getGamePhase());

        //Move along the faith track, but does not instantly win
        assertFalse(b.execute(lorenzoController));

        gc.getGame().getLorenzo().addPoints(21);

        //Move along the faith track and win the game by reaching 24 faith points
        assertTrue(c.execute(lorenzoController));

    }

    @Test
    void executeGeneralActionTest() {
        Player test = new Player("test");
        gc.getGame().addPlayer(test);
        gc.getGame().setCurrentPlayer(test);
        while (gc.getGame().getGamePhase() != GamePhase.END) {
            lorenzoController.executeAction();
        }

        assertEquals(GamePhase.END, gc.getGame().getGamePhase());
    }
}