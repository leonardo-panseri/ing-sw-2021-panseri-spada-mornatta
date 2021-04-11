package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.List;

public class MarketResultUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = 5345288716406897205L;

    private String playerName;
    private List<Resource> result;

    public MarketResultUpdate(String playerName, List<Resource> result) {
        this.playerName = playerName;
        this.result = result;
    }

    @Override
    public void process(View view) {

    }
}
