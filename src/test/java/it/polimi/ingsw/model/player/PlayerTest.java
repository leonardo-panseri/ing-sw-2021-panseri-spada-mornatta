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

    private Game testGame;
    private Player player;
    private Map<Resource, Integer> testResourceRequirements;
    private Map<CardColor, Integer> testCardColorRequirements;
    private Map<CardColor, Integer> testCardLevelRequirements;
    private LeaderCard testLeaderCard;
    private List<LeaderCard> testListLeaderCards;


    @BeforeEach
    public void initialization() {
        testGame = new Game();
        player = new Player("Edoardo");
        testGame.addPlayer(player);
        
        SpecialAbility testSpecialAbility = new SpecialAbility(SpecialAbilityType.DEPOT, Resource.COIN);
        testResourceRequirements = Map.of(Resource.COIN, 2);
        testCardColorRequirements = Map.of(CardColor.BLUE, 2);
        testCardLevelRequirements = Map.of(CardColor.BLUE, 2);
        LeaderCardRequirement testLeaderCardRequirement =
                new LeaderCardRequirement(testResourceRequirements, testCardColorRequirements, testCardLevelRequirements);
        testLeaderCard = new LeaderCard(1, testLeaderCardRequirement, testSpecialAbility);
        testListLeaderCards = Collections.singletonList(testLeaderCard);
    }

    @Test
    void getFaithPoints() {
        int testFaithPoint1 = 5;
        int testFaithPoint2 = 13;
        int testFaithPoint3 = 15;
        player.setFaithPoints(testFaithPoint1);
        assertEquals(testFaithPoint1, player.getFaithPoints());
        player.setFaithPoints(testFaithPoint2);
        assertEquals(testFaithPoint2, player.getFaithPoints());
        player.setFaithPoints(testFaithPoint3);
        assertEquals(testFaithPoint3, player.getFaithPoints());
    }

    @Test
    void setFaithPoints() {
        player.setFaithPoints(10);
        assertEquals(10, player.getFaithPoints());
        player.setFaithPoints(20);
        assertEquals(20, player.getFaithPoints());
        player.setFaithPoints(7);
        assertEquals(7, player.getFaithPoints());
    }

    @Test
    void getPopeFavours() {
        player.setPopeFavours(1);
        assertEquals(1, player.getPopeFavours());
        player.setPopeFavours(2);
        assertEquals(2, player.getPopeFavours());
        player.setPopeFavours(3);
        assertEquals(3, player.getPopeFavours());
    }

    @Test
    void setPopeFavours() {
        player.setPopeFavours(1);
        assertEquals(1, player.getPopeFavours());
        player.setPopeFavours(2);
        assertEquals(2, player.getPopeFavours());
        player.setPopeFavours(3);
        assertEquals(3, player.getPopeFavours());
    }

    @Test
    void setLeaderActive() {
        player.setLeaderCards(testListLeaderCards);
        player.setLeaderActive(testLeaderCard);
        assertTrue(player.isLeaderActive(testLeaderCard));
        assertThrows(IllegalArgumentException.class, () -> {
            player.setLeaderActive(testLeaderCard);
        });

        assertThrows(IllegalArgumentException.class,
                () -> {
                    LeaderCard card = getTestLeaderCardWithAbility(new SpecialAbility(SpecialAbilityType.DEPOT, Resource.COIN));
                    player.setLeaderActive(card);
                });

    }

    @Test
    void setLeaderCards() {
        player.setLeaderCards(testListLeaderCards);
        assertTrue(player.getLeaderCards().containsKey(testLeaderCard));
    }


    @Test
    void addFaithPoints() {
        player.setFaithPoints(0);
        player.addFaithPoints(3);
        assertEquals(3, player.getFaithPoints());
        player.setFaithPoints(15);
        player.addFaithPoints(5);
        assertEquals(20, player.getFaithPoints());
        player.setFaithPoints(0);
        player.addFaithPoints(5);
        assertEquals(5, player.getFaithPoints());
    }

    @Test
    void discardLeader() {
        player.setLeaderCards(testListLeaderCards);
        player.discardLeader(testLeaderCard);
        assertTrue(player.getLeaderCards().isEmpty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            player.discardLeader(testLeaderCard);
        });

        assertThrows(IllegalArgumentException.class,
                () -> {
                    player.setLeaderCards(testListLeaderCards);
                    player.setLeaderActive(testLeaderCard);
                    player.discardLeader(testLeaderCard);
                    ;
                });
    }

    @Test
    public void countLeadersDiscount() {
        LeaderCard card1 = getTestLeaderCardWithAbility(new SpecialAbility(SpecialAbilityType.DISCOUNT, Resource.SHIELD));
        LeaderCard card2 = getTestLeaderCardWithAbility(new SpecialAbility(SpecialAbilityType.DISCOUNT, Resource.SHIELD));

        player.setLeaderCards(Arrays.asList(card1, card2));
        player.setLeaderActive(card1);
        player.setLeaderActive(card2);

        assertEquals(2, player.numLeadersDiscount(Resource.SHIELD));
        assertEquals(0, player.numLeadersDiscount(Resource.COIN));
    }

    @Test
    public void hasLeaderWhiteConversion() {
        LeaderCard card = getTestLeaderCardWithAbility(new SpecialAbility(SpecialAbilityType.EXCHANGE, Resource.STONE));

        player.setLeaderCards(Collections.singletonList(card));
        player.setLeaderActive(card);

        assertTrue(player.hasLeaderWhiteConversion(Resource.STONE));
        assertFalse(player.hasLeaderWhiteConversion(Resource.SHIELD));
    }

    @Test
    public void hasLeaderDeposits() {
        LeaderCard card1 = getTestLeaderCardWithAbility(new SpecialAbility(SpecialAbilityType.DEPOT, Resource.COIN));
        LeaderCard card2 = getTestLeaderCardWithAbility(new SpecialAbility(SpecialAbilityType.DEPOT, Resource.SERVANT));

        player.setLeaderCards(Arrays.asList(card1, card2));
        player.setLeaderActive(card1);
        player.setLeaderActive(card2);

        assertTrue(player.hasLeaderDeposits(Arrays.asList(Resource.COIN, Resource.SERVANT)));
        assertFalse(player.hasLeaderDeposits(Collections.singletonList(Resource.SHIELD)));
    }

    @Test
    public void addPopeFavours() {
        player.addPopeFavours(1);
        assertEquals(1, player.getPopeFavours());
        player.addPopeFavours(2);
        assertEquals(3, player.getPopeFavours());
        player.addPopeFavours(3);
        assertEquals(6, player.getPopeFavours());
    }

    @Test
    public void keepLeaders(){
        LeaderCard cardPresent = getTestLeaderCardWithAbility(new SpecialAbility(SpecialAbilityType.DEPOT, Resource.COIN));
        LeaderCard cardNotPresent = getTestLeaderCardWithAbility(new SpecialAbility(SpecialAbilityType.DEPOT, Resource.SERVANT));
        player.setLeaderCards(Arrays.asList(testLeaderCard, cardPresent, cardNotPresent));

        List<LeaderCard> chosen = new ArrayList<>(Arrays.asList(testLeaderCard, cardPresent));
        player.keepLeaders(chosen);

        assertTrue(player.getLeaderCards().containsKey(testLeaderCard));
        assertTrue(player.getLeaderCards().containsKey(cardPresent));
        assertFalse(player.getLeaderCards().containsKey(cardNotPresent));

        chosen.remove(1);
        chosen.add(cardNotPresent);
        assertThrows(IllegalArgumentException.class, () -> player.keepLeaders(chosen));
    }

    @Test
    public void getLeaderCardByUuid() {
        player.setLeaderCards(testListLeaderCards);
        assertNotNull(player.getLeaderCardByUuid(testLeaderCard.getUuid()));
        player.discardLeader(testLeaderCard);
        assertNull(player.getLeaderCardByUuid(testLeaderCard.getUuid()));
    }

    @Test
    public void getLeaderCardsTotalVictoryPoints() {
        LeaderCard card = getTestLeaderCardWithAbility(new SpecialAbility(SpecialAbilityType.DISCOUNT, Resource.SERVANT));
        player.setLeaderCards(Arrays.asList(testLeaderCard, card));
        player.setLeaderActive(testLeaderCard);
        player.setLeaderActive(card);

        assertEquals(7, player.getLeaderCardsTotalVictoryPoints());
    }

    private LeaderCard getTestLeaderCardWithAbility(SpecialAbility ability) {
        return new LeaderCard(6,
                new LeaderCardRequirement(testResourceRequirements, testCardColorRequirements, testCardLevelRequirements),
                ability);
    }
}