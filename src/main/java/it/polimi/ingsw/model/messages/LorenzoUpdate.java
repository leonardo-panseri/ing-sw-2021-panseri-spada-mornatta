package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * Update sent upon modification of Lorenzo's faith points (in single player mode).
 */
public class LorenzoUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = 5123184263333073010L;

    private final int faithPoints;

    /**
     * Constructor: creates a new LorenzoUpdate.
     * @param points the new Lorenzo faith points
     **/
    public LorenzoUpdate(int points) {
        faithPoints = points;
    }

    @Override
    public void process(View view) {

    }
}
