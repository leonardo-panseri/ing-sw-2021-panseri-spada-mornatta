package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.implementation.gui.widget.LeaderCardWidget;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditLeaderCards {
    @FXML
    private VBox leaderCardsContainer;

    private ListView<LeaderCardWidget> leaderCardsView;

    @FXML
    private void goToHome() {
        GameConfigEditor.goToHome();
    }

    @FXML
    private void initialize() {
        List<LeaderCardWidget> leaderCardWidgets = new ArrayList<>();
        GameConfigEditor.getGameConfig().getLeaderCards().forEach(card -> leaderCardWidgets.add(new LeaderCardWidget(card)));
        ObservableList<LeaderCardWidget> observableLeaderCardWidgets = FXCollections.observableList(leaderCardWidgets);

        leaderCardsView = new ListView<>(observableLeaderCardWidgets);
        VBox.setVgrow(leaderCardsView, Priority.ALWAYS);

        leaderCardsContainer.getChildren().add(leaderCardsView);

        leaderCardsView.setCellFactory(leaderCardWidgetListView -> new EditableLeaderCardCell());
        leaderCardsView.setOnMouseClicked(mouseEvent ->
                goToLeaderCardEdit(leaderCardsView.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void createNewLeaderCard() {
        int victoryPoints;
        Map<Resource, Integer> resourceRequirements = new HashMap<>();
        Map<CardColor, Integer> cardColorRequirements = new HashMap<>();
        Map<CardColor, Integer> cardLevelRequirements = new HashMap<>();

        //Default card parameters
        victoryPoints = 0;
        SpecialAbilityType specialAbilityType = SpecialAbilityType.PRODUCTION;
        Resource targetResource = Resource.STONE;
        resourceRequirements.put(Resource.SERVANT, 1);

        LeaderCardRequirement requirement = new LeaderCardRequirement(resourceRequirements, cardColorRequirements,
                cardLevelRequirements);
        SpecialAbility specialAbility = new SpecialAbility(specialAbilityType, targetResource);

        LeaderCard newDefaultCard = new LeaderCard(victoryPoints, requirement, specialAbility);
        LeaderCardWidget newDefaultWidget = new LeaderCardWidget(newDefaultCard);

        GameConfigEditor.getGameConfig().addNewLeaderCard(newDefaultCard);
        leaderCardsView.getItems().add(newDefaultWidget);
        goToLeaderCardEdit(newDefaultWidget);
    }

    private void goToLeaderCardEdit(LeaderCardWidget leaderCardWidget) {
        if (leaderCardWidget == null)
            return;

        EditLeaderCard editLeaderCard = new EditLeaderCard(leaderCardWidget);
        GameConfigEditor.goToEditLeaderCard(editLeaderCard);
    }
}

final class EditableLeaderCardCell extends ListCell<LeaderCardWidget> {
    @Override
    protected void updateItem(LeaderCardWidget item, boolean empty) {
        super.updateItem(item, empty);

        if (empty)
            setGraphic(null);
        else {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().add(item);
            setGraphic(hBox);
        }

    }
}
