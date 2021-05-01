package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.beans.MockPlayer;

import java.util.List;
import java.util.Map;

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

    public abstract void printOwnDeposit();

    public abstract void printOwnStrongbox();

    public abstract void printOthersLeaderCards(String playerName);

    public abstract void printOthersDevelopmentCards(String playerName);

    public abstract void printOthersDeposit(String playerName);

    public abstract void printOthersFaith(String playerName);

    public abstract void printDevelopmentDeck();

    public abstract void printFaith(int faith);

    public abstract void printMarketResult();

    public abstract void printChatMessage(String sender, String message);

    public abstract void printFinalScores(Map<String, Integer> scores, String winnerName);

    public abstract void printSingleplayerFinalScore(boolean lorenzoWin, String loseReason, int playerScore);

    public abstract void renderDevelopmentCard(DevelopmentCard card, int label);

    public abstract void renderLeaderCard(LeaderCard card, int label);

    public abstract void renderDeposit(MockPlayer player);

    public abstract void renderLeadersDeposit(MockPlayer player);

    public abstract void renderStrongbox(MockPlayer player);

    public abstract void renderMarket(List<List<Resource>> grid, Resource slideResource);

    public abstract String renderResource(Resource res);

    public abstract void help();
}
