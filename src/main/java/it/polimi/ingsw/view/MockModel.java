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

    /**
     * Initializes all the necessary attributes of the MockModel.
     */
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
        for (int i = 0; i < 3; i++) {
            developmentCards.add(new Stack<>());
            deposit.add(new ArrayList<>());
        }

    }

    /**
     * Gets a map corresponding to the LeaderCards of the player.
     *
     * @return a map of the LeaderCards.
     */

    public Map<LeaderCard, Boolean> getLeaderCards() {
        return leaderCards;
    }

    /**
     * Sets the LeaderCards of the player
     *
     * @param leaderCards a map containing the LeaderCards that need to be set
     */

    public void setLeaderCards(Map<LeaderCard, Boolean> leaderCards) {
        this.leaderCards = leaderCards;
    }


    /**
     * Gets the leader cards of the other players.
     *
     * @return a map containing the leader cards of the other players
     */
    public Map<String, Map<LeaderCard, Boolean>> getOtherLeaderCards() {
        return otherLeaderCards;
    }

    /**
     * Sets the leader cards  of the given player or Lorenzo's development cards in the event of a single player game.
     *
     * @param playerName  the name of the player to perform the set on.
     * @param leaderCards a map made of the leader cards of a given non current player
     */
    public void setOthersLeaderCards(String playerName, Map<LeaderCard, Boolean> leaderCards) {
        otherLeaderCards.put(playerName, leaderCards);
    }

    /**
     * Gets the development cards of the other players or Lorenzo's development cards
     * in the event of a single player game.
     *
     * @return a map representing the development cards of the other players or Lorenzo's development cards in the event
     * of a single player game
     */
    public Map<String, List<Stack<DevelopmentCard>>> getOtherDevelopmentCards() {
        return otherDevelopmentCards;
    }

    /**
     * Sets the the development card of the given player or Lorenzo's in the event of a single player game.
     *
     * @param card the development card that needs to be set
     * @param slot the slot in which the development card needs to be put in
     */
    public void setOtherNewDevelopment(String playerName, DevelopmentCard card, int slot) {
        otherDevelopmentCards.get(playerName).get(slot - 1).push(card);
    }

    /**
     * Sets the faith points of a given player or Lorenzo's in the event of a single player game.
     *
     * @param playerName  the name of the player to set faith points to
     * @param faithPoints the number of faith points that need to be added to the given player
     */
    public void setOtherFaith(String playerName, int faithPoints) {
        otherFaith.put(playerName, faithPoints);
    }

    /**
     * Gets the faith points of a given player or Lorenzo's in the event of a single player game.
     *
     * @param playerName the name of the player to get faith points from
     * @return Gets the faith points of a given player or Lorenzo's in the event of a single player game
     */
    public int getOtherFaith(String playerName) {
        return this.otherFaith.containsKey(playerName) ? otherFaith.get(playerName) : 0;
    }

    /**
     * Gets the deposit of the non current players.
     *
     * @return a map representing the deposit of the non current players
     */

    public Map<String, List<List<Resource>>> getOtherDeposit() {
        return otherDeposit;
    }

    /**
     * Gets the deposit of the leader cards with deposit as their special ability type of the other players.
     *
     * @return a Map representing  the deposit of the leader cards with deposit as their
     * special ability type of the other players.
     */
    public Map<String, Map<Integer, List<Resource>>> getOtherLeadersDeposit() {
        return otherLeadersDeposit;
    }

    /**
     * Sets the pope favours of the other players.
     *
     * @param playerName  the name of the player to set the pope favours to.
     * @param popeFavours the pope favour that need to be added to the given player
     */
    public void setOtherFavours(String playerName, int popeFavours) {
        otherFavours.put(playerName, popeFavours);
    }

    /**
     * Gets the deck made up of development cards.
     *
     * @return a list representing the deck made up of development cards
     */
    public List<HashMap<CardColor, Stack<DevelopmentCard>>> getDevelopmentDeck() {
        return developmentDeck;
    }

    /**
     * Sets the deck made up of development cards.
     *
     * @param developmentDeck a stack representing the deck made up of development cards, each stack initially has
     *                        3 cards
     */
    public void setDevelopmentDeck(List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck) {
        this.developmentDeck = developmentDeck;
    }

    /**
     * Gets the deposit of the current player.
     *
     * @return a list of a list of resources representing the deposit of the current player
     */
    public List<List<Resource>> getDeposit() {
        return deposit;
    }

    /**
     * Sets the deposit of the current player.
     *
     * @param deposit a list of a list of resources representing the deposit of the current player
     */
    public void setDeposit(List<List<Resource>> deposit) {
        this.deposit = deposit;
    }

    /**
     * Gets the strongbox of the current player.
     *
     * @return a map showing the number of times that the resources are present in the strongbox
     */
    public Map<Resource, Integer> getStrongbox() {
        return strongbox;
    }

    /**
     * Sets the strongbox of the current player.
     *
     * @param strongbox a map showing the number of times that the resources are present in the strongbox
     */

    /**
     * Sets the strongbox of the current player.
     *
     * @param strongbox a Map representing the strongbox, the integer
     *                  represents the number of times the resource is present in the strongbox
     */
    public void setStrongbox(Map<Resource, Integer> strongbox) {
        this.strongbox = strongbox;
    }

    /**
     * Gets the development cards of the current player.
     *
     * @return a list representing the development cards of the current player
     */
    public List<Stack<DevelopmentCard>> getDevelopmentCards() {
        return developmentCards;
    }

    /**
     * Shows if the current player has any development cards
     *
     * @return returns true if the current player has at least one development card, false if
     *         the player does not have any.
     */
    public boolean hasOwnDevelopmentCard() {
        for (Stack<DevelopmentCard> stack : developmentCards) {
            if (!stack.isEmpty()) return true;
        }
        return false;
    }

    /**
     * Sets a new development card to the current player.
     *
     * @param card the development card needed to be set
     * @param slot the slot in which the card will be put
     */
    public void setNewDevelopmentCard(DevelopmentCard card, int slot) {
        developmentCards.get(slot - 1).push(card);
    }

    /**
     * Gets the market.
     *
     * @return a list of a list representing the market used to draw resources
     */
    public List<List<Resource>> getMarket() {
        return market;
    }

    /**
     * Sets the market.
     *
     * @param market list of a list representing the market used to draw resources
     */
    public void setMarket(List<List<Resource>> market) {
        this.market = market;
    }

    /**
     * Updates a row of the market.
     *
     * @param index the index representing the row that needs to be changed
     * @param changes a List containing the resources that will be associated to the new row
     */
    public void updateMarketRow(int index, List<Resource> changes) {
        getMarket().get(index).clear();
        getMarket().get(index).addAll(changes);
    }

    /**
     * Gets faith points of the player.
     *
     * @return the number of faith points held by the player
     */
    public int getFaithPoints() {
        return this.faithPoints;
    }

    /**
     * Sets the faith points of the current player.
     *
     * @param faithPoints the number of faith points that need to be added to the current player
     */

    public void setFaithPoints(int faithPoints) {
        this.faithPoints = faithPoints;
    }

    /**
     * Gets the number of pope favours of the current player.
     *
     * @return the number of pope favours of the current player
     */
    public int getPopeFavours() {
        return popeFavours;
    }

    /**
     * Sets the number of pope favours of the current player.
     *
     * @param popeFavours the number of pope favours of the current player
     */
    public void setPopeFavours(int popeFavours) {
        this.popeFavours = popeFavours;
    }

    /**
     * Update a column of the market.
     *
     * @param index the index representing the column that needs to be changed
     * @param changes a List containing the resources that will be associated to the new column
     */
    public void updateMarketColumn(int index, List<Resource> changes) {
        for (List<Resource> row : getMarket()) {
            row.remove(index);
            row.add(index, changes.get(getMarket().indexOf(row)));
        }
    }

    /**
     * Gets the result of a draw from the market.
     *
     * @return the result of a draw from the market.
     */
    public List<Resource> getMarketResult() {
        return marketResult;
    }

    /**
     * Sets the result of a draw from the market.
     *
     * @param marketResult a list representing the result of a draw from the market
     */
    public void setMarketResult(List<Resource> marketResult) {
        this.marketResult = marketResult;
    }

    /**
     * Gets the deposit of the leader cards having deposit as their special ability.
     *
     * @return a Map showing the deposit of the leaders
     */

    public Map<Integer, List<Resource>> getLeadersDeposit() {
        return leadersDeposit;
    }

    /**
     * Sets the deposit of the leader cards having deposit as their special ability.
     *
     * @param leadersDeposit a Map showing the deposit of the leaders
     */
    public void setLeadersDeposit(Map<Integer, List<Resource>> leadersDeposit) {
        this.leadersDeposit = leadersDeposit;
    }

    /**
     * Counts the occurrences of a white resource in a given row or column of the market.
     *
     * @param index the index representing the row or column of the market
     * @return the occurrences of a white resource in the row or column of the market
     */
    public int countWhiteResources(int index) {
        int result = 0;
        if (index > 0 && index < 5) {
            for (int i = 0; i < 3; i++) {
                if (market.get(i).get(index - 1) == null) result++;
            }
        } else if (index > 4 && index < 8) {
            for (List<Resource> row : market) {
                for (Resource res : row) {
                    if (res == null) result++;
                }
            }
        }
        return result;
    }

    /**
     * Checks if the player has two leader cards with deposit as their special ability type.
     *
     * @return true if the player has two leader cards with deposit as their special ability type,
     * else false
     */
    public boolean hasTwoLeaderDeposits() {
        int count = 0;
        for (LeaderCard card : leaderCards.keySet()) {
            if (card.getSpecialAbility().getType() == SpecialAbilityType.DEPOT) count++;
        }
        return count == 2;
    }
}
