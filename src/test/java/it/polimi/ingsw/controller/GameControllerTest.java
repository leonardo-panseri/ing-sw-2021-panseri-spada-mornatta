package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.BaseProductionPower;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.messages.InitialTurnUpdate;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.view.messages.EndTurnPlayerActionEvent;
import it.polimi.ingsw.view.messages.MarketPlayerActionEvent;
import it.polimi.ingsw.view.messages.PlayerActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameController controllerTest;

    @BeforeEach
    void setUp() {
        controllerTest = new GameController(new Lobby());
    }

    @Test
    void getterSetterTest() {
        assertFalse(controllerTest.isSinglePlayer());
        assertNotNull(controllerTest.getGame());
        assertNotNull(controllerTest.getPlayerController());
        assertNotNull(controllerTest.getTurnController());
        assertNull(controllerTest.getLorenzoController());
    }

    @Test
    void addPlayerTest() {
        Player p1 = new Player("one");
        Player p2 = new Player("two");
        controllerTest.addPlayer(p1);
        assertEquals(1, controllerTest.getGame().getPlayerCount());
        controllerTest.addPlayer(p2);
        assertEquals(2, controllerTest.getGame().getPlayerCount());

        assertEquals("one", controllerTest.getGame().getPlayerAt(0).getNick());
        assertEquals("two", controllerTest.getGame().getPlayerAt(1).getNick());
    }

    @Test
    void endGameTest() {
        Player p1 = new Player("one");
        Player p2 = new Player("two");
        controllerTest.addPlayer(p1);
        controllerTest.addPlayer(p2);
        HashMap<Resource, Integer> res = new HashMap<>();
        res.put(Resource.STONE, 5);

        controllerTest.getGame().getPlayerAt(0).getBoard().getDeposit()
                .addMultipleToStrongbox(res);

        try {
            controllerTest.endGame();
        } catch (IllegalMonitorStateException ignored) {
        }

        assertEquals(GamePhase.END, controllerTest.getGame().getGamePhase());
    }

    @Test
    void updateTest() {
        Player test = new Player("test");
        Player test2 = new Player("test2");
        PlayerActionEvent act = new EndTurnPlayerActionEvent();
        act.setPlayer(test);
        controllerTest.getGame().getMarket().initializeMarket();
        PlayerActionEvent draw = new MarketPlayerActionEvent(1, new ArrayList<>()); // Draw move to allow player to end turn
        draw.setPlayer(test);

        controllerTest.getGame().addPlayer(test);
        controllerTest.getGame().addPlayer(test2);
        controllerTest.getGame().setCurrentPlayer(test);

        controllerTest.update(act);
        assertEquals("test", controllerTest.getGame().getCurrentPlayer().getNick());

        //Update turn setting the next current player correctly
        controllerTest.update(draw);
        controllerTest.update(act);
        assertEquals("test2", controllerTest.getGame().getCurrentPlayer().getNick());

        //Ignore the sent action because it is sent by a non current player
        controllerTest.update(act);
        assertEquals("test2", controllerTest.getGame().getCurrentPlayer().getNick());
    }

    @Test
    void customGameConfig() {
        Lobby lobby = new Lobby();
        BaseProductionPower productionPower = new BaseProductionPower(Arrays.asList(Resource.COIN, Resource.SERVANT), Arrays.asList(Resource.FAITH, Resource.STONE));
        try {
            Field customGameConfig = lobby.getClass().getDeclaredField("customGameConfig");
            customGameConfig.setAccessible(true);
            GameConfig config = GameConfig.loadDefaultGameConfig();
            assert config != null;
            config.modifyBaseProduction(productionPower);
            customGameConfig.set(lobby, config);
            Field isGameConfigSet = lobby.getClass().getDeclaredField("isGameConfigSet");
            isGameConfigSet.setAccessible(true);
            isGameConfigSet.set(lobby, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        GameController gameController = new GameController(lobby);
        assertEquals(productionPower.getInput(), gameController.getGame().getBaseProductionPower().getInput());
        assertEquals(productionPower.getOutput(), gameController.getGame().getBaseProductionPower().getOutput());
    }

    @Test
    void checkAlreadyPlayed() {
        Player p1 = new Player("one");

        controllerTest.addPlayer(p1);

        assertDoesNotThrow(() -> controllerTest.checkAlreadyPlayed(p1));

        p1.setHasAlreadyPlayed(true);

        assertThrows(IllegalStateException.class, () -> controllerTest.checkAlreadyPlayed(p1));
    }
}