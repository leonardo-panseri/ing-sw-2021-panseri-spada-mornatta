package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;

import java.util.*;

public class MockModel {

    private Map<LeaderCard, Boolean> leaderCards;
    private List<Stack<DevelopmentCard>> developmentCards;
    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck;
    private List<List<Resource>> deposit;
    private Map<Resource, Integer> strongbox;
    private List<List<Resource>> market;
    private List<Resource> marketResult;
    private Map<Integer, List<Resource>> leadersDeposit;
    int faithPoints = 15;
    int popeFavours = 2;

    private final Map<String, Map<LeaderCard, Boolean>> otherLeaderCards;
    private final Map<String, List<Stack<DevelopmentCard>>> otherDevelopmentCards;
    private final Map<String, List<List<Resource>>> otherDeposit;
    private final Map<String, Map<Integer, List<Resource>>> otherLeadersDeposit;
    private final Map<String, Integer> otherFaith;
    private final Map<String, Integer> otherFavours;

    public MockModel() {
        otherLeaderCards = new HashMap<>();
        otherDevelopmentCards = new HashMap<>();
        otherDeposit = new HashMap<>();
        otherLeadersDeposit = new HashMap<>();
        otherFaith = new HashMap<>();
        otherFavours = new HashMap<>();
        developmentCards = new ArrayList<>();
        deposit = new ArrayList<>();
        leadersDeposit = new HashMap<>();
        leadersDeposit.put(1, new ArrayList<>());
        leadersDeposit.put(2, new ArrayList<>());
        strongbox = new HashMap<>();
        marketResult = new ArrayList<>();
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

    public Map<String, Map<LeaderCard, Boolean>> getOtherLeaderCards() {
        return otherLeaderCards;
    }

    public void setOthersLeaderCards(String playerName, Map<LeaderCard, Boolean> leaderCards) {
        otherLeaderCards.put(playerName, leaderCards);
    }

    public Map<String, List<Stack<DevelopmentCard>>> getOtherDevelopmentCards() {
        return otherDevelopmentCards;
    }

    public void setOtherNewDevelopment(String playerName, DevelopmentCard card, int slot) {
        otherDevelopmentCards.get(playerName).get(slot - 1).push(card);
    }

    public void setOtherFaith(String playerName, int faithPoints) {
        otherFaith.put(playerName, faithPoints);
    }

    public int getOtherFaith(String playerName) {
        return this.otherFaith.containsKey(playerName) ? otherFaith.get(playerName) : 0;
    }

    public Map<String, List<List<Resource>>> getOtherDeposit() {
        return otherDeposit;
    }

    public Map<String, Map<Integer, List<Resource>>> getOtherLeadersDeposit() {
        return otherLeadersDeposit;
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

    public Map<Resource, Integer> getStrongbox() {
        return strongbox;
    }

    public void setStrongbox(Map<Resource, Integer> strongbox) {
        this.strongbox = strongbox;
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

    public Map<Integer, List<Resource>> getLeadersDeposit() {
        return leadersDeposit;
    }

    public void setLeadersDeposit(Map<Integer, List<Resource>> leadersDeposit) {
        this.leadersDeposit = leadersDeposit;
    }

    public int countWhiteResources(int index) {
        int result = 0;
        if(index > 0 && index < 5) {
            for(int i = 0; i < 3; i++) {
                if(market.get(i).get(index - 1) == null) result++;
            }
        } else if(index > 4 && index < 8) {
            for(List<Resource> row : market) {
                for(Resource res : row) {
                    if(res == null) result++;
                }
            }
        }
        return result;
    }

    public boolean hasTwoLeaderDeposits() {
        int count = 0;
        for(LeaderCard card : leaderCards.keySet()) {
            if(card.getSpecialAbility().getType() == SpecialAbilityType.DEPOT) count++;
        }
        return count == 2;
    }
}
