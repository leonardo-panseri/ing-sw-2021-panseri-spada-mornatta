package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.editor.controller.DeleteLeaderCard;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.Renderer;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.beans.MockPlayer;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class GUIRenderer extends Renderer {
    protected GUIRenderer(View view) {
        super(view);
    }

    @Override
    public void showGameMessage(String message) {

    }

    @Override
    public void showLobbyMessage(String message) {

    }

    @Override
    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    @Override
    public void printMarket() {

    }

    @Override
    public void printOwnLeaders() {

    }

    @Override
    public void printOwnDevelopmentCards() {

    }

    @Override
    public void printOwnDeposit() {

    }

    @Override
    public void printOwnStrongbox() {

    }

    @Override
    public void printOthersLeaderCards(String playerName) {

    }

    @Override
    public void printOthersDevelopmentCards(String playerName) {

    }

    @Override
    public void printOthersDeposit(String playerName) {

    }

    @Override
    public void printOthersFaith(String playerName) {

    }

    @Override
    public void printOthersFavours(String playerName) {

    }

    @Override
    public void printDevelopmentDeck() {

    }

    @Override
    public void printFaith(int faith) {

    }

    @Override
    public void printFavours(int popeFavours) {

    }

    @Override
    public void printMarketResult() {

    }

    @Override
    public void printChatMessage(String sender, String message) {

    }

    @Override
    public void printFinalScores(Map<String, Integer> scores, String winnerName) {

    }

    @Override
    public void printSingleplayerFinalScore(boolean lorenzoWin, String loseReason, int playerScore) {

    }

    @Override
    public void renderDevelopmentCard(DevelopmentCard card, int label) {

    }

    @Override
    public void renderLeaderCard(LeaderCard card, int label) {

    }

    @Override
    public void renderDeposit(MockPlayer player) {

    }

    @Override
    public void renderLeadersDeposit(MockPlayer player) {

    }

    @Override
    public void renderStrongbox(MockPlayer player) {

    }

    @Override
    public void renderMarket(List<List<Resource>> grid, Resource slideResource) {

    }

    @Override
    public String renderResource(Resource res) {
        return null;
    }

    @Override
    public void help() {

    }
}
