package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CardsTest {
    private Map<Resource, Integer> cost, prodIn, prodOut;
    private DevelopmentCard testDevelopmentCard;

    private Map<Resource, Integer> resourceReq;
    private Map<CardColor, Integer> cardColorReq;
    private Map<CardColor, Integer> cardLevelReq;
    private LeaderCard testLeaderCard;

    private Player testRichPlayer;
    private Player testPoorPlayer;

    public static DevelopmentCard createTestDevCard(int victoryPoints, int level) {
        Map<Resource, Integer> req = new HashMap<>();
        req.put(Resource.STONE, 2);
        return new DevelopmentCard(12, req, 1, new HashMap<>(), new HashMap<>(), CardColor.GREEN);
    }

    @BeforeEach
    void setUp() {
        cost = Map.of(Resource.SERVANT, 2, Resource.SHIELD, 3);
        prodIn = Map.of(Resource.COIN, 1, Resource.STONE, 1);
        prodOut = Map.of(Resource.FAITH, 2);
        testDevelopmentCard = new DevelopmentCard(10, cost, 1,
                prodIn, prodOut, CardColor.GREEN);

        resourceReq = Map.of(Resource.SERVANT, 2, Resource.SHIELD, 3);
        cardColorReq = Map.of(CardColor.GREEN, 2);
        cardLevelReq = Map.of(CardColor.GREEN, 2);
        LeaderCardRequirement requirements = new LeaderCardRequirement(resourceReq, cardColorReq, cardLevelReq);
        SpecialAbility specialAbility = new SpecialAbility(SpecialAbilityType.EXCHANGE, Resource.SHIELD);
        testLeaderCard = new LeaderCard(7, requirements, specialAbility);

        testRichPlayer = new Player("TestRich");
        testRichPlayer.getBoard().getDeposit().addMultipleToStrongbox(Map.of(Resource.SERVANT, 2, Resource.SHIELD, 4));
        testPoorPlayer = new Player("TestPoor");
        testPoorPlayer.getBoard().getDeposit().addMultipleToStrongbox(Map.of(Resource.SERVANT, 1, Resource.SHIELD, 1,
                Resource.STONE, 2));
    }

    @Test
    void developmentCardConstructionTest() {
        assertNotNull(testDevelopmentCard.getUuid());
        assertEquals(testDevelopmentCard.getVictoryPoints(), 10);
        assertEquals(testDevelopmentCard.getCost(), new HashMap<>(cost));
        assertEquals(testDevelopmentCard.getLevel(), 1);
        assertEquals(testDevelopmentCard.getProductionInput(), new HashMap<>(prodIn));
        assertEquals(testDevelopmentCard.getProductionOutput(), new HashMap<>(prodOut));
        assertEquals(testDevelopmentCard.getColor(), CardColor.GREEN);
    }

    @Test
    void canPlayerAffordDevelopmentCardTest() {
        assertTrue(testDevelopmentCard.canPlayerAfford(testRichPlayer));
        assertFalse(testDevelopmentCard.canPlayerAfford(testPoorPlayer));
    }

    @Test
    void leaderCardConstructionTest() {
        assertNotNull(testLeaderCard.getUuid());
        assertEquals(testLeaderCard.getVictoryPoints(), 7);
        assertEquals(testLeaderCard.getCardRequirements().getResourceRequirements(), new HashMap<>(resourceReq));
        assertEquals(testLeaderCard.getCardRequirements().getCardColorRequirements(), new HashMap<>(cardColorReq));
        assertEquals(testLeaderCard.getCardRequirements().getCardLevelRequirements(), new HashMap<>(cardLevelReq));
        assertEquals(testLeaderCard.getSpecialAbility().getType(), SpecialAbilityType.EXCHANGE);
        assertEquals(testLeaderCard.getSpecialAbility().getTargetResource(), Resource.SHIELD);
    }

    @Test
    void canPlayerAffordLeaderCardTest() {
        testRichPlayer.getBoard().addCard(1, testDevelopmentCard);
        testRichPlayer.getBoard().addCard(1, new DevelopmentCard(10, cost, 2,
                prodIn, prodOut, CardColor.GREEN));

        assertTrue(testLeaderCard.canPlayerAfford(testRichPlayer));
        assertFalse(testLeaderCard.canPlayerAfford(testPoorPlayer));
    }

    public static DevelopmentCard getTestDevelopmentCard(int victoryPoints, Map<Resource, Integer> cost, int level,
                                                         Map<Resource, Integer> prodIn, Map<Resource, Integer> prodOut,
                                                         CardColor color) {
        return new DevelopmentCard(victoryPoints, cost, level,
                prodIn, prodOut, color);
    }
}
