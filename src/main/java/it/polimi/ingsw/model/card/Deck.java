package it.polimi.ingsw.model.card;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<LeaderCard> leaderCards;
    private List<DevelopmentCard> developmentCards;

    public Deck() {
        leaderCards = new ArrayList<>();
        developmentCards = new ArrayList<>();
    }

    public List<LeaderCard> initialDrawLeaders(){
        List<LeaderCard> result = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            int randomNumber = (int) (Math.random() * (leaderCards.size() - 1));
            result.add(leaderCards.get(randomNumber));
            leaderCards.remove(randomNumber);
        }
        return result;
    }

    public void removeBoughtCard(DevelopmentCard card){
        developmentCards.remove(card);
    }

}
