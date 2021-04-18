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
    private List<List<Resource>> market;
    private List<Resource> marketResult;
    int faithPoints = 15;
    int popeFavours = 2;

    private Map<String, Map<LeaderCard, Boolean>> otherLeaderCards;
    private Map<String, List<Stack<DevelopmentCard>>> otherDevelopmentCards;
    private Map<String, List<Resource>> otherDeposit;
    private Map<String, Integer> otherFaith;
    private Map<String, Integer> otherFavours;

    public MockModel() {
        otherLeaderCards = new HashMap<>();
        otherFaith = new HashMap<>();
        developmentCards = new ArrayList<>();
        deposit = new ArrayList<>();
        for (int i = 0; i<3; i++) {
            developmentCards.add(new Stack<>());
            deposit.add(new ArrayList<>());
        }
        List<List<Resource>> testList = new ArrayList<>();
        List<Resource> topRow = new ArrayList<>();
        List<Resource> middleRow = new ArrayList<>();
        List<Resource> bottomRow = new ArrayList<>();
        topRow.add(Resource.COIN);
        middleRow.add(Resource.STONE);
        middleRow.add(Resource.STONE);
        bottomRow.add(Resource.FAITH);
        bottomRow.add(Resource.FAITH);
        bottomRow.add(Resource.FAITH);
        testList.add(topRow);
        testList.add(middleRow);
        testList.add(bottomRow);
        this.setDeposit(testList);

    }

    public Map<LeaderCard, Boolean> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(Map<LeaderCard, Boolean> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public Map<String, Map<LeaderCard, Boolean>> getOtherLeaderCards() {
        return otherLeaderCards;
    }

    public void setOthersLeaderCards(String playerName, Map<LeaderCard, Boolean> leaderCards) {
        otherLeaderCards.put(playerName, leaderCards);
    }

    public Map<String, List<Stack<DevelopmentCard>>> getOtherDevelopmentCards() {
        return otherDevelopmentCards;
    }

    public void setOtherFaith(String playerName, int faithPoints) {
        otherFaith.put(playerName, faithPoints);
    }

    public int getOtherFaith(String playerName) {
        return this.otherFaith.containsKey(playerName) ? otherFaith.get(playerName) : 0;
    }

    public void setOtherFavours(String playerName, int popeFavours) {
        otherFavours.put(playerName, popeFavours);
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

    public boolean hasOwnDevelopmentCard() {
        for (Stack<DevelopmentCard> stack : developmentCards) {
            if (!stack.isEmpty()) return true;
        }
        return false;
    }

    public void setNewDevelopmentCard(DevelopmentCard card, int slot) {
        developmentCards.get(slot - 1).push(card);
    }

    public List<List<Resource>> getMarket() {
        return market;
    }

    public void setMarket(List<List<Resource>> market) {
        this.market = market;
    }

    public void updateMarketRow(int index, List<Resource> changes) {
        getMarket().get(index).clear();
        getMarket().get(index).addAll(changes);
    }

    public int getFaithPoints(){
        return this.faithPoints;
    }

    public void setFaithPoints(int faithPoints) {
        this.faithPoints = faithPoints;
    }

    public int getPopeFavours() {
        return popeFavours;
    }

    public void setPopeFavours(int popeFavours) {
        this.popeFavours = popeFavours;
    }

    public void updateMarketColumn(int index, List<Resource> changes) {
        for (List<Resource> row : getMarket()) {
            row.remove(index);
            row.add(index, changes.get(getMarket().indexOf(row)));
        }
    }

    public List<Resource> getMarketResult() {
        return marketResult;
    }

    public void setMarketResult(List<Resource> marketResult) {
        this.marketResult = marketResult;
    }
}
