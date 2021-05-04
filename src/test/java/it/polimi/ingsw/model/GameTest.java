package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messages.ChatUpdate;
import it.polimi.ingsw.model.messages.InvalidActionUpdate;
import it.polimi.ingsw.model.messages.PropertyUpdate;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.IServerPacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Player player;
    private Player player2;

    @BeforeEach
    void setUp() {
        game = new Game();
        player = new Player("Edoardo");
        player2 = new Player("Davide");
    }

    @Test
    void addPlayer() {
        assertEquals(0, game.getPlayers().size());
        game.addPlayer(player);
        assertTrue(game.getPlayers().contains(player));
        assertEquals(1, game.getPlayers().size());
    }

    @Test
    void setCurrentPlayer() {
        assertNull(game.getCurrentPlayer());
        assertThrows(IllegalArgumentException.class, () -> game.setCurrentPlayer(player));
        game.addPlayer(player);
        game.setCurrentPlayer(player);
        assertEquals(player, game.getCurrentPlayer());
    }

    @Test
    void getPlayerNum() {
        assertEquals(0, game.getPlayerCount());
        game.addPlayer(player);
        assertEquals(1, game.getPlayerCount());
        game.addPlayer(player2);
        assertEquals(2, game.getPlayerCount());
    }

    @Test
    void setGamePhase() {
        assertEquals(GamePhase.SELECTING_LEADERS, game.getGamePhase());
        game.setGamePhase(GamePhase.PLAYING);
        assertEquals(GamePhase.PLAYING, game.getGamePhase());
        game.setGamePhase(GamePhase.LAST_ROUND);
        assertEquals(GamePhase.LAST_ROUND, game.getGamePhase());
        game.setGamePhase(GamePhase.END);
        assertEquals(GamePhase.END, game.getGamePhase());
    }

    @Test
    void startLastRound() {
        assertEquals(GamePhase.SELECTING_LEADERS, game.getGamePhase());
        game.startLastRound(player);
        assertTrue(game.isLastRound());
        game.startLastRound(player);
        assertTrue(game.isLastRound());
    }

    @Test
    void nextPlayer() {
        game.addPlayer(player);
        game.addPlayer(player2);
        game.setCurrentPlayer(player);
        assertEquals(player, game.getCurrentPlayer());
        game.nextPlayer();
        assertEquals(player2, game.getCurrentPlayer());
        game.nextPlayer();
        assertEquals(player, game.getCurrentPlayer());
    }

    @Test
    void isLastPlayerTurn() {
        game.addPlayer(player);
        game.addPlayer(player2);
        game.setCurrentPlayer(player);

        assertFalse(game.isLastPlayerTurn());
        game.nextPlayer();
        assertTrue(game.isLastPlayerTurn());
    }

    @Test
    void createLorenzo() {
        assertNull(game.getLorenzo());
        game.createLorenzo();
        assertNotNull(game.getLorenzo());
    }

    @Test
    void randomSortPlayers() {
        game.addPlayer(player);
        game.addPlayer(player2);
        assertEquals(2, game.getPlayerCount());
        game.randomSortPlayers();
        assertEquals(2, game.getPlayerCount());
        assertTrue(game.getPlayers().contains(player));
        assertTrue(game.getPlayers().contains(player2));
    }

    @Test
    void terminate() {
        assertEquals(GamePhase.SELECTING_LEADERS, game.getGamePhase());
        game.terminate(Collections.emptyMap(), "Edoardo");
        assertEquals(GamePhase.END, game.getGamePhase());
    }

    @Test
    void terminateSingleplayer() {
        assertEquals(GamePhase.SELECTING_LEADERS, game.getGamePhase());
        game.terminateSingleplayer(true, "", 1);
        assertEquals(GamePhase.END, game.getGamePhase());
    }

    @Test
    void calculateFaithTrackVictoryPoints() {
        //TODO after implementing configurable games
    }

    @Test
    void checkForPopeReportSlot() {
        //Same as above
    }

    @Test
    void activatePopeReport() {
        //Same as above
    }

    @Test
    void notifyChatMessage() {
        List<IServerPacket> updates = new ArrayList<>();
        game.addObserver(updates::add);
        game.notifyChatMessage("Davide", "jngl diff");
        assertEquals(1, updates.size());
        assertTrue(updates.get(0) instanceof ChatUpdate);
    }

    @Test
    void notifyInvalidAction() {
        List<IServerPacket> updates = new ArrayList<>();
        game.addObserver(updates::add);
        game.notifyInvalidAction(player, "jngl diff");
        assertEquals(1, updates.size());
        assertTrue(updates.get(0) instanceof InvalidActionUpdate);
    }
}