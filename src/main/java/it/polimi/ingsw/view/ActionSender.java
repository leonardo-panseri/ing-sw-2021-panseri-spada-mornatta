package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Resource;

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
}
