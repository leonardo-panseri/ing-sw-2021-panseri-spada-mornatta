package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.lorenzo.Lorenzo;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerTest;
import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TurnControllerTest {

    private Lobby lobby;
    private GameController gameController;
    private TurnController turnController;
    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        lobby = new Lobby();
        setLobbyPlayersToStart(2);
        gameController = new GameController(lobby);
        turnController = gameController.getTurnController();
        game = gameController.getGame();
        player1 = new Player("Edoardo");
        player2 = new Player("Davide");
        game.addPlayer(player1);
        game.addPlayer(player2);
    }

    @Test
    void start() {
        assertNotNull(game.getPlayerAt(0));
        assertNotNull(game.getPlayerAt(1));

        turnController.start();

        assertTrue(game.getPlayers().contains(player1));
        assertTrue(game.getPlayers().contains(player2));
        assertEquals(game.getPlayerAt(0), game.getCurrentPlayer());
        for(Player p : game.getPlayers()) {
            Map<LeaderCard, Boolean> leaderCards = PlayerTest.getPlayersLeadercards(p);
            assertEquals(4, leaderCards.size());
            leaderCards.forEach((card, active) -> assertFalse(active));
        }
        assertEquals(GamePhase.SELECTING_LEADERS, game.getGamePhase());
    }

    @Test
    void wrongPlayerEndsTurn() {
        turnController.start();
        assertThrows(IllegalStateException.class, () -> turnController.endTurn(getPlayerNotPlaying()));
    }

    @RepeatedTest(5)
    void endTurnAddFaithMulti() {
        turnController.start();
        Player currentPlayer = game.getCurrentPlayer();
        game.getMarket().initializeMarket();
        gameController.getPlayerController().useMarket(currentPlayer, 4, new ArrayList<>());
        int marketResultsCount = currentPlayer.getBoard().getDeposit().getUnusedMarketResults();
        int faithCurrent = currentPlayer.getFaithPoints();
        int faithOther = getPlayerNotPlaying().getFaithPoints();
        turnController.endTurn(currentPlayer);
        assertEquals(faithCurrent, currentPlayer.getFaithPoints());
        assertEquals(faithOther + marketResultsCount, game.getCurrentPlayer().getFaithPoints());
    }

    @Test
    void endGameMulti() {
        turnController.start();
        turnController.endTurn(game.getCurrentPlayer());
        game.setGamePhase(GamePhase.LAST_ROUND);
        turnController.endTurn(game.getCurrentPlayer());

        assertEquals(GamePhase.END, game.getGamePhase());
    }

    private void setLobbyPlayersToStart(int playersToStart) {
        try {
            Field field = lobby.getClass().getDeclaredField("playersToStart");
            field.setAccessible(true);
            field.set(lobby, playersToStart);
        } catch (NoSuchFieldException|IllegalAccessException e) {
            System.err.println("Error setting lobby playersToStart with reflections");
        }
    }

    private Player getPlayerNotPlaying() {
        return player1.equals(game.getCurrentPlayer()) ? player2 : player1;
    }

    @Nested
    class TurnControllerSinglePlayerTest {
        private Lorenzo lorenzo;

        @BeforeEach
        void setUp() {
            lobby = new Lobby();
            setLobbyPlayersToStart(1);
            gameController = new GameController(lobby);
            turnController = gameController.getTurnController();
            game = gameController.getGame();
            player1 = new Player("Edoardo");
            game.addPlayer(player1);
            lorenzo = game.createLorenzo();
        }

        @RepeatedTest(5)
        void endTurnAddFaithSingle() {
            turnController.start();
            game.getMarket().initializeMarket();
            gameController.getPlayerController().useMarket(player1, 5, new ArrayList<>());
            int marketResultsCount = player1.getBoard().getDeposit().getUnusedMarketResults();
            int faithLorenzo = lorenzo.getFaithPoints();
            turnController.endTurn(player1);
            assertTrue(faithLorenzo + marketResultsCount <= lorenzo.getFaithPoints());
        }

        @Test
        void endGameSingle() {
            turnController.start();
            game.setGamePhase(GamePhase.LAST_ROUND);
            turnController.endTurn(game.getCurrentPlayer());

            assertEquals(GamePhase.END, game.getGamePhase());
        }
    }
}