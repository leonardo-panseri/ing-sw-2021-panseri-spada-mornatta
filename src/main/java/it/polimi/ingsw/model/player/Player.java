package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.Map;

public class Player {
    private String nick;
    private int faithPoints;
    private int popeFavours;
    private Map<LeaderCard, Boolean> leaderCards;
    private PlayerBoard board;

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

    }

    public void discardLeader(LeaderCard leaderCard) {

    }

    public void buyCard(DevelopmentCard developmentCard) {

    }

    public void useMarket() {

    }

    public void useProduction(DevelopmentCard developmentCard) {

    }

    public void endTurn() {

    }
}
