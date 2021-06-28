package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.beans.MockPlayer;

import java.util.List;
import java.util.Map;

/**
 * Class responsible of game components rendering, mainly used for the CLI.
 */
public abstract class Renderer {
    private final View view;

    protected Renderer(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void showGameMessage(String message){}

    public void showLobbyMessage(String message){}

    public abstract void showErrorMessage(String message);

    public void printMarket(){}

    public void printOwnLeaders(){}

    public void printOwnDevelopmentCards(){}

    public void printOwnDeposit(){}

    public void printOwnStrongbox(){}

    public void printOthersLeaderCards(String playerName){}

    public void printOthersDevelopmentCards(String playerName){}

    public void printOthersDeposit(String playerName){}

    public void printOthersFaith(String playerName){}

    public void printOthersFavours(String playerName){}

    public void printDevelopmentDeck(){}

    public void printFaith(int faith){}

    public void printFavours(int popeFavours){}

    public void printMarketResult(){}

    public void printChatMessage(String sender, String message){}

    public void printFinalScores(Map<String, Integer> scores, String winnerName){}

    public void printSingleplayerFinalScore(boolean lorenzoWin, String loseReason, int playerScore){}

    public void renderDevelopmentCard(DevelopmentCard card, int label){}

    public void renderLeaderCard(LeaderCard card, int label){}

    public void renderDeposit(MockPlayer player){}

    public void renderLeadersDeposit(MockPlayer player){}

    public void renderStrongbox(MockPlayer player){}

    public void renderMarket(List<List<Resource>> grid, Resource slideResource){}

    public String renderResource(Resource res){
        return "";
    }

    public void help(){}
}
