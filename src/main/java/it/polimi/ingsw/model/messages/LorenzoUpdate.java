package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class LorenzoUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = 5123184263333073010L;

    private final int faithPoints;

    public LorenzoUpdate(int points) {
        faithPoints = points;
    }

    @Override
    public void process(View view) {

    }
}
