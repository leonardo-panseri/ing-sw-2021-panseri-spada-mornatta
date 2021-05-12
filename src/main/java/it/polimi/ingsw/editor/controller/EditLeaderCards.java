package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class EditLeaderCards {
    @FXML
    public VBox leaderCardsContainer;

    @FXML
    public void goToHome() {
        GameConfigEditor.goToHome();
    }

    @FXML
    public void initialize() {
        List<LeaderCardWidget> leaderCardWidgets = new ArrayList<>();
        GameConfigEditor.getGameConfig().getLeaderCards().forEach(card -> leaderCardWidgets.add(new LeaderCardWidget(card)));
        ObservableList<LeaderCardWidget> observableLeaderCardWidgets = FXCollections.observableList(leaderCardWidgets);

        ListView<LeaderCardWidget> leaderCardsView = new ListView<>(observableLeaderCardWidgets);
        leaderCardsView.getStyleClass().add("leaderCardList");

        leaderCardsContainer.getChildren().add(leaderCardsView);
    }
}
