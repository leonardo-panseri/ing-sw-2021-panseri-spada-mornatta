package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private String nick;
    private int faithPoints;
    private int popeFavours;
    private Map<LeaderCard, Boolean> leaderCards;
    private PlayerBoard board;

    public Player(String nick) {
        this.nick = nick;
        faithPoints = 0;
        popeFavours = 0;
        board = new PlayerBoard();
    }

    public String getNick() {
        return nick;
    }

    public int getFaithPoints() {
        return faithPoints;
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

    public void setLeaderActive(LeaderCard leaderCard) {
        leaderCards.put(leaderCard, true);
    }

    public void setLeaderCards(List<LeaderCard> cards) {
        if (leaderCards == null) leaderCards = new HashMap<>();
        for (LeaderCard leaderCard : cards) {
            leaderCards.put(leaderCard, false);
        }
    }
}
