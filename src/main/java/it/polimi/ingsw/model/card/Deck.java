package it.polimi.ingsw.model.card;

import it.polimi.ingsw.constant.DeckParser;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.*;

public class Deck extends Observable<PropertyUpdate> {
    private List<LeaderCard> leaderCards;
    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards;

    public Deck() {
        leaderCards = DeckParser.loadLeaderCards();
        developmentCards = DeckParser.loadDevelopmentCards();
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : developmentCards){
            for (Stack<DevelopmentCard> stack : map.values()){
                Collections.shuffle(stack);
            }
        }
    }

    public List<LeaderCard> initialDrawLeaders() {
        List<LeaderCard> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int randomNumber = (int) (Math.random() * (leaderCards.size()));
            while (randomNumber == leaderCards.size()) randomNumber = (int) (Math.random() * (leaderCards.size()));
            result.add(leaderCards.get(randomNumber));
            leaderCards.remove(randomNumber);
        }
        return result;
    }

    public void removeBoughtCard(DevelopmentCard card) {
        developmentCards
                .get(card.getLevel() - 1)
                .get(card.getColor())
                .pop();
    }

}
