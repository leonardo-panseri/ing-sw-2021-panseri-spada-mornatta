package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.messages.ChatUpdate;
import it.polimi.ingsw.model.messages.InvalidActionUpdate;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.IServerPacket;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.view.messages.production.BaseProduction;
import it.polimi.ingsw.view.messages.production.DevelopmentProduction;
import it.polimi.ingsw.view.messages.production.LeaderProduction;
import it.polimi.ingsw.view.messages.production.Production;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {

    private GameController gc;
    private PlayerController playerController;
    private Player p1;
    List<IServerPacket> updates;
    LeaderCard card1;
    LeaderCard card2;
    LeaderCard card3;

    @BeforeEach
    void setUp() {
        gc = new GameController(new Lobby());
        playerController = gc.getPlayerController();
        p1 = new Player("one");
        gc.getGame().addPlayer(p1);
        gc.getGame().setCurrentPlayer(p1);

        updates = new ArrayList<>();
        gc.getGame().addObserver(updates::add);

        Map<Resource, Integer> resMap = new HashMap<>();
        resMap.put(Resource.STONE, 2);
        LeaderCardRequirement req = new LeaderCardRequirement(resMap, new HashMap<>(), new HashMap<>());
        card1 = new LeaderCard(12, req, new SpecialAbility(SpecialAbilityType.DISCOUNT, Resource.STONE));
        card2 = new LeaderCard(10, req, new SpecialAbility(SpecialAbilityType.DEPOT, Resource.SERVANT));
        card3 = new LeaderCard(9, req, new SpecialAbility(SpecialAbilityType.PRODUCTION, Resource.SERVANT));
    }

    @Test
    void activateLeaderTest() {

        List<LeaderCard> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        p1.setLeaderCards(cards);

        //Player cannot afford the card
        playerController.activateLeaderCard(p1, card1);
        assertFalse(p1.isLeaderActive(card1));

        //Player can afford the card
        Map<Resource, Integer> cheat = new HashMap<>();
        for (Resource res : Resource.values()) {
            cheat.put(res, 100);
        }
        p1.getBoard().getDeposit().addMultipleToStrongbox(cheat);
        playerController.activateLeaderCard(p1, card1);
        assertTrue(p1.isLeaderActive(card1));

        //Player does not have the requested card
        List<IServerPacket> updates = new ArrayList<>();
        gc.getGame().addObserver(updates::add);
        playerController.activateLeaderCard(p1, card3);
        assertTrue(updates.get(0) instanceof InvalidActionUpdate);
    }

    @Test
    void buyDevelopmentTest() {
        DevelopmentCard devCard = CardsTest.createTestDevCard(12, 1);

        //Player wants to buy a card that is not in the deck
        playerController.buyDevelopmentCard(p1, devCard, 1);
        assertTrue(updates.get(0) instanceof InvalidActionUpdate);

        //Player does not have enough resources
        DevelopmentCard card = DeckTest.getCardByLevel(1, gc.getGame().getDeck());
        playerController.buyDevelopmentCard(p1, card, 1);
        assertTrue(updates.get(1) instanceof InvalidActionUpdate);

        //Player can buy the card
        Map<Resource, Integer> cheat = new HashMap<>();
        for (Resource res : Resource.values()) {
            cheat.put(res, 100);
        }
        p1.getBoard().getDeposit().addMultipleToStrongbox(cheat);
        playerController.buyDevelopmentCard(p1, card, 1);
        assertTrue(p1.getBoard().hasCardOfColorAndLevel(CardColor.GREEN, 1));

        //Player cannot place the card in the board
        DevelopmentCard card2 = DeckTest.getCardByLevel(2, gc.getGame().getDeck());
        playerController.buyDevelopmentCard(p1, card2, 2);
        assertTrue(updates.get(2) instanceof InvalidActionUpdate);
    }

    @Test
    void updateDepositTest() {
        Map<Integer, List<Resource>> changes = new HashMap<>();
        List<Resource> changedRow = new ArrayList<>();
        changedRow.add(Resource.COIN);
        changes.put(2, changedRow);

        //Not a valid deposit update: player creates new resources
        playerController.updatePlayerDeposit(p1, changes, new ArrayList<>(), new HashMap<>());
        assertTrue(updates.get(0) instanceof InvalidActionUpdate);

        p1.getBoard().getDeposit().setMarketResults(changedRow);
        //Legal move
        playerController.updatePlayerDeposit(p1, changes, new ArrayList<>(), new HashMap<>());
        assertEquals(0, p1.getBoard().getDeposit().getMarketResults().size());
        assertEquals(1, p1.getBoard().getDeposit().countAllResources());
    }

    @Test
    void chooseAndDiscardLeaderTest() {
        List<LeaderCard> chosenLeaders = new ArrayList<>();
        chosenLeaders.add(card1);
        chosenLeaders.add(card2);
        chosenLeaders.add(card3);

        //Player has not selected exactly 2 cards
        playerController.selectInitialLeaders(p1, chosenLeaders);
        assertTrue(updates.get(0) instanceof InvalidActionUpdate);

        //Player has selected some cards he does not have in hands
        chosenLeaders.remove(card3);
        playerController.selectInitialLeaders(p1, chosenLeaders);
        assertTrue(updates.get(1) instanceof InvalidActionUpdate);

        //Player correctly selects 2 leaders
        List<LeaderCard> ownLeaders = new ArrayList<>(chosenLeaders);
        ownLeaders.add(card3);
        p1.setLeaderCards(ownLeaders);
        playerController.selectInitialLeaders(p1, chosenLeaders);
        p1.setLeaderActive(card1);
        p1.setLeaderActive(card2);
        assertEquals(1, p1.numLeadersDiscount(Resource.STONE));
        assertFalse(p1.hasLeaderDeposits(Arrays.asList(Resource.SERVANT, Resource.SERVANT)));

        //Player tries to discard an active leader
        playerController.discardLeader(p1, card1);
        assertTrue(updates.get(4) instanceof InvalidActionUpdate);

        //Player discard an inactive leader
        p1.setLeaderCards(Collections.singletonList(card3));
        playerController.discardLeader(p1, card3);
        assertEquals(1, p1.getFaithPoints());
    }

    @Test
    void useMarketTest() {
        gc.getGame().getMarket().initializeMarket();
        //Index out of bound
        playerController.useMarket(p1, 7, new ArrayList<>());
        assertTrue(updates.get(0) instanceof InvalidActionUpdate);

        //Player does not have the requested white conversions
        playerController.useMarket(p1, 2, Arrays.asList(Resource.STONE, Resource.SERVANT));
        assertTrue(updates.get(1) instanceof InvalidActionUpdate);

        //Player draws a row
        playerController.useMarket(p1, 5, new ArrayList<>());

        //Player draws a column
        playerController.useMarket(p1, 2, new ArrayList<>());
        assertTrue(() -> {
            for (int i = 2; i < updates.size(); i++) {
                if (updates.get(i) instanceof InvalidActionUpdate) return false;
            }
            return true;
        });
    }

    @Test
    void useProductions() {
        //Test all the production types
        Production prod1 = new BaseProduction(Arrays.asList(Resource.STONE, Resource.STONE), Resource.FAITH);
        Production prod2 = new DevelopmentProduction(DeckTest.getCardByLevel(1, gc.getGame().getDeck()).getUuid());
        Production prod3 = new LeaderProduction(card3.getUuid(), Resource.SHIELD);

        //Test some other specific cases
        Production prod4 = new BaseProduction(Arrays.asList(Resource.STONE, Resource.COIN), Resource.SERVANT);

        List<Production> prods = Arrays.asList(prod1, prod2, prod3, prod4);

        //Player cannot use any of these productions
        playerController.useProductions(p1, prods);
        for (IServerPacket update : updates) {
            assertTrue(update instanceof InvalidActionUpdate);
        }

        //Player can perform all the productions
        Map<Resource, Integer> cheat = new HashMap<>();
        for (Resource res : Resource.values()) {
            cheat.put(res, 100);
        }
        p1.getBoard().getDeposit().addMultipleToStrongbox(cheat);
        p1.getBoard().addCard(1, DeckTest.getCardByLevel(1, gc.getGame().getDeck()));
        p1.setLeaderCards(Collections.singletonList(card3));
        p1.setLeaderActive(card3);
        playerController.useProductions(p1, prods);
        assertEquals(4, updates.size());
    }

    @Test
    void sendChatMessage() {
        playerController.sendChatMessage(p1, "Hello World");
        assertTrue(updates.get(0) instanceof ChatUpdate);
    }
}