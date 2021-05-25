package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.constant.AnsiColor;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.ModelUpdateHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.implementation.gui.widget.PlayerBoardWidget;

import java.util.List;
import java.util.Map;

public class GUIModelUpdateHandler extends ModelUpdateHandler {
    protected GUIModelUpdateHandler(View view) {
        super(view);
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        super.updateGamePhase(gamePhase);
        if (gamePhase == GamePhase.SELECTING_LEADERS) {
            GUI.instance().showPlayerBoard();
        }
    }
}
