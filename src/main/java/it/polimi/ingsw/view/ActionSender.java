package it.polimi.ingsw.view;

import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.messages.EndTurnPlayerActionEvent;

import java.util.Map;

public abstract class ActionSender {
    private final View view;

    public ActionSender(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public abstract void buyDevelopmentCard(int cardIndex);

    public abstract void draw(int marketIndex, Resource whiteConversion);

    public abstract void discard(int cardIndex);

    public abstract void move(int row1, int row2);

    public void endTurn() {
        if(!view.isOwnTurn()) {
            view.getRenderer().showErrorMessage(ViewString.NOT_YOUR_TURN);
            return;
        }
        if(!view.hasAlreadyPlayed()) {
            view.getRenderer().showErrorMessage(ViewString.NOT_ALREADY_PLAYED);
            return;
        }
        view.getClient().send(new EndTurnPlayerActionEvent(view.getPlayerName()));
        view.setAlreadyPlayed(false);
    }
    public abstract void setActive(int cardIndex);
}
