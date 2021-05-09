package it.polimi.ingsw.view;

import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.messages.EndTurnPlayerActionEvent;
import it.polimi.ingsw.view.messages.production.Production;

import java.util.ArrayList;
import java.util.List;

public abstract class ActionSender {
    private final View view;

    private final List<Production> pendingProductions;

    public ActionSender(View view) {
        this.view = view;
        this.pendingProductions = new ArrayList<>();
    }

    public View getView() {
        return view;
    }

    public List<Production> getPendingProductions() {
        return pendingProductions;
    }

    protected void addPendingProduction(Production production) {
        pendingProductions.add(production);
    }

    protected void clearPendingProductions() {
        pendingProductions.clear();
    }

    public abstract void buyDevelopmentCard(int cardIndex, int slotIndex);

    public abstract void draw(int marketIndex, List<Resource> whiteConversions);

    public abstract void discard(int cardIndex);

    public abstract void move(int row1, int row2);

    public abstract void storeMarketResult(int resourceIndex, int rowIndex);

    public void endTurn() {
        if(!view.isOwnTurn()) {
            view.getRenderer().showErrorMessage(ViewString.NOT_YOUR_TURN);
            return;
        }

        view.getClient().send(new EndTurnPlayerActionEvent());
        view.setUsingProductions(false);
    }
    public abstract void setActive(int cardIndex);

    public abstract void useLeaderProduction(int cardIndex, Resource desiredResource);

    public abstract void useDevelopmentProduction(int cardIndex);

    public abstract void useBaseProduction(List<Resource> inputResource, List<Resource> outputResource);

    public abstract void executeProductions();

    public abstract void sendChatMessage(String message);
}
