package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

public abstract class Renderer {
    private final View view;

    protected Renderer(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public abstract void showGameMessage(String message);

    public abstract void showLobbyMessage(String message);

    public abstract void showErrorMessage(String message);

    public abstract void printMarket();

    public abstract void printOwnLeaders();

    public abstract void printOwnDevelopmentCards();

    public abstract void printOthersDevelopmentCards(String playerName);

    public abstract void printDevelopmentDeck();

    public abstract void printDeposit();

    public abstract void printFaith();

    public abstract void renderDevelopmentCard(DevelopmentCard card, int label);

    public abstract void renderLeaderCard(LeaderCard card, int label);
}
