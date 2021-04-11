package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.player.Player;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Game testGame;
    PlayerBoard testPlayerBoard;
    Map<Resource, Integer> testResourceRequirements = new HashMap<>();
    Map<CardColor, Integer> testCardColorRequirements = new HashMap<>();
    Map<CardColor, Integer> testCardLevelRequirements = new HashMap<>();
    LeaderCard testLeaderCard;
    LeaderCard testLeaderCard2;
    Map<LeaderCard, Boolean> testListLeaderCards = new HashMap<>();
    Map<LeaderCard, Boolean> testListLeaderCards2 = new HashMap<>();


    @BeforeEach
    public void initialization() {
        testGame = new Game();
        testGame.addPlayer(new Player("Edoardo"));
        testGame.addPlayer(new Player("Davide"));
        testGame.addPlayer(new Player("Leonardo"));
        SpecialAbility testSpecialAbility = new SpecialAbility(SpecialAbilityType.DEPOT, Resource.COIN);
        testResourceRequirements.put(Resource.COIN, 2);
        testCardColorRequirements.put(CardColor.BLUE, 2);
        testCardLevelRequirements.put(CardColor.BLUE, 2);
        LeaderCardRequirement testLeaderCardRequirement =
                new LeaderCardRequirement(testResourceRequirements, testCardColorRequirements, testCardLevelRequirements);
        testLeaderCard = new LeaderCard(1, testLeaderCardRequirement, testSpecialAbility);
        testListLeaderCards.put(testLeaderCard, false);


    }

    @Test
    void getNick() {
        // HOW?
    }

    @Test
    void getFaithPoints() {
        int testFaithPoint1 = 5;
        int testFaithPoint2 = 13;
        int testFaithPoint3 = 15;
        testGame.getPlayerByName("Edoardo").setFaithPoints(5);
        testGame.getPlayerByName("Davide").setFaithPoints(13);
        testGame.getPlayerByName("Leonardo").setFaithPoints(15);
        assertEquals(5, testGame.getPlayerByName("Edoardo").getFaithPoints());
        assertEquals(13, testGame.getPlayerByName("Davide").getFaithPoints());
        assertEquals(15, testGame.getPlayerByName("Leonardo").getFaithPoints());
    }

    @Test
    void getBoard() {
        // useful?
    }

    @Test
    void setFaithPoints() {

        testGame.getPlayerByName("Edoardo").setFaithPoints(10);
        assertEquals(10, testGame.getPlayerByName("Edoardo").getFaithPoints());
        testGame.getPlayerByName("Davide").setFaithPoints(20);
        assertEquals(20, testGame.getPlayerByName("Davide").getFaithPoints());
        testGame.getPlayerByName("Leonardo").setFaithPoints(7);
        assertEquals(7, testGame.getPlayerByName("Leonardo").getFaithPoints());
    }

    @Test
    void getPopeFavours() {
        testGame.getPlayerByName("Edoardo").setPopeFavours(1);
        assertEquals(1, testGame.getPlayerByName("Edoardo").getPopeFavours());
        testGame.getPlayerByName("Edoardo").setPopeFavours(2);
        assertEquals(2, testGame.getPlayerByName("Edoardo").getPopeFavours());
        testGame.getPlayerByName("Edoardo").setPopeFavours(3);
        assertEquals(3, testGame.getPlayerByName("Edoardo").getPopeFavours());
    }

    @Test
    void setPopeFavours() {
        testGame.getPlayerByName("Edoardo").setPopeFavours(1);
        assertEquals(1, testGame.getPlayerByName("Edoardo").getPopeFavours());
        testGame.getPlayerByName("Edoardo").setPopeFavours(2);
        assertEquals(2, testGame.getPlayerByName("Edoardo").getPopeFavours());
        testGame.getPlayerByName("Edoardo").setPopeFavours(3);
        assertEquals(3, testGame.getPlayerByName("Edoardo").getPopeFavours());
    }

    @Test
    void setLeaderActive() {
        testGame.getPlayerByName("Edoardo").setLeaderCards(new ArrayList<>(testListLeaderCards.keySet()));
        testGame.getPlayerByName("Edoardo").setLeaderActive(testLeaderCard);
        assert (testGame.getPlayerByName("Edoardo").getLeaderCards().get(testLeaderCard));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            testGame.getPlayerByName("Edoardo").setLeaderActive(testLeaderCard);
        });

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    testGame.getPlayerByName("Edoardo").setLeaderActive(testLeaderCard);
                });
        String expectedMessage = "leadercard_already_active";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));


        exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    SpecialAbility testSpecialAbility = new SpecialAbility(SpecialAbilityType.DEPOT, Resource.COIN);
                    testCardLevelRequirements.put(CardColor.BLUE, 1);
                    LeaderCardRequirement testLeaderCardRequirement1 =
                            new LeaderCardRequirement(testResourceRequirements, testCardColorRequirements, testCardLevelRequirements);
                    testLeaderCard = new LeaderCard(1, testLeaderCardRequirement1, testSpecialAbility);
                    testListLeaderCards.put(testLeaderCard2, false);
                    testGame.getPlayerByName("Edoardo").setLeaderCards(new ArrayList<>(testListLeaderCards2.keySet()));
                    testGame.getPlayerByName("Edoardo").setLeaderActive(testLeaderCard);
                });
        expectedMessage = "leadercard_not_present";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void setLeaderCards() {
        testGame.getPlayerByName("Edoardo").setLeaderCards(new ArrayList<>(testListLeaderCards.keySet()));
        assert (testGame.getPlayerByName("Edoardo").getLeaderCards().containsKey(testLeaderCard));


    }


    @Test
    void addFaithPoints() {
        testGame.getPlayerByName("Edoardo").setFaithPoints(0);
        testGame.getPlayerByName("Edoardo").addFaithPoints(3);
        assertEquals(3, testGame.getPlayerByName("Edoardo").getFaithPoints());
        testGame.getPlayerByName("Davide").setFaithPoints(15);
        testGame.getPlayerByName("Davide").addFaithPoints(5);
        assertEquals(20, testGame.getPlayerByName("Davide").getFaithPoints());
        testGame.getPlayerByName("Leonardo").setFaithPoints(0);
        testGame.getPlayerByName("Leonardo").addFaithPoints(5);
        assertEquals(5, testGame.getPlayerByName("Leonardo").getFaithPoints());
    }

    @Test
    void discardLeader() {
        testGame.getPlayerByName("Edoardo").setLeaderCards(new ArrayList<>(testListLeaderCards.keySet()));
        testGame.getPlayerByName("Edoardo").discardLeader(testLeaderCard);
        assert (testGame.getPlayerByName("Edoardo").getLeaderCards().isEmpty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            testGame.getPlayerByName("Edoardo").discardLeader(testLeaderCard);
        });

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    testGame.getPlayerByName("Edoardo").discardLeader(testLeaderCard);
                });
        String expectedMessage = "leadercard_not_present";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    testGame.getPlayerByName("Edoardo").setLeaderCards(new ArrayList<>(testListLeaderCards.keySet()));
                    testGame.getPlayerByName("Edoardo").setLeaderActive(testLeaderCard);
                    testGame.getPlayerByName("Edoardo").discardLeader(testLeaderCard);
                    ;
                });
        expectedMessage = "leadercard_already_active";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}