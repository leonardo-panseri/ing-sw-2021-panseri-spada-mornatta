package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.*;

public class MockModel {

    private Map<LeaderCard, Boolean> leaderCards;
    private List<Stack<DevelopmentCard>> developmentCards;
    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck;
    private List<List<Resource>> deposit;
    private String market;

    public MockModel() {
        developmentCards = new ArrayList<>();
        deposit = new ArrayList<>();
        for (int i = 0; i<3; i++) {
            developmentCards.add(new Stack<>());
            deposit.add(new ArrayList<>());
        }
    }

    public Map<LeaderCard, Boolean> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(Map<LeaderCard, Boolean> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public List<HashMap<CardColor, Stack<DevelopmentCard>>> getDevelopmentDeck() {
        return developmentDeck;
    }

    public void setDevelopmentDeck(List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck) {
        this.developmentDeck = developmentDeck;
    }

    public List<List<Resource>> getDeposit() {
        return deposit;
    }

    public void setDeposit(List<List<Resource>> deposit) {
        this.deposit = deposit;
    }

    public List<Stack<DevelopmentCard>> getDevelopmentCards() {
        return developmentCards;
    }

    public void setNewDevelopmentCard(DevelopmentCard card, int slot) {
        developmentCards.get(slot - 1).push(card);
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
}