package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.model.card.LeaderCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        VBox.setVgrow(leaderCardsView, Priority.ALWAYS);

        leaderCardsContainer.getChildren().add(leaderCardsView);

        leaderCardsView.setCellFactory(leaderCardWidgetListView -> new EditableLeaderCardCell());
        leaderCardsView.setOnMouseClicked(mouseEvent ->
            goToLeaderCardEdit(leaderCardsView.getSelectionModel().getSelectedItem()));
    }

    private void goToLeaderCardEdit(LeaderCardWidget leaderCardWidget) {
        EditLeaderCard editLeaderCard = new EditLeaderCard(leaderCardWidget);
        GameConfigEditor.goToEditLeaderCard(editLeaderCard);
    }
}

final class EditableLeaderCardCell extends ListCell<LeaderCardWidget> {
    @Override
    protected void updateItem(LeaderCardWidget item, boolean empty) {
        super.updateItem(item, empty);

        if(empty)
            setGraphic(null);
        else {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().add(item);
            setGraphic(hBox);
        }

    }
}
