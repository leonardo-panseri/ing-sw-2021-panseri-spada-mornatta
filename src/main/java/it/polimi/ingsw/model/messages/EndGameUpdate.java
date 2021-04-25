package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.util.Map;

public class EndGameUpdate extends PropertyUpdate {
    private final Map<String, Integer> scores;
    private final String winnerName;

    public EndGameUpdate(Map<String, Integer> scores, String winnerName) {
        this.scores = scores;
        this.winnerName = winnerName;
    }

    @Override
    public void process(View view) {
        //TODO Implement
    }
}
