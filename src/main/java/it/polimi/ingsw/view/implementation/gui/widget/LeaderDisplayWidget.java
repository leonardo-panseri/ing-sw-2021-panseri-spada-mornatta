package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.Group;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class LeaderDisplayWidget extends VBox {
    @FXML
    public VBox leaderDisplay;

    private final MockPlayer player;
    private final Map<LeaderCard, LeaderCardWidget> leadersAndWidgets;

    public LeaderDisplayWidget(MockPlayer player) {
        this.player = player;
        this.leadersAndWidgets = new HashMap<>();

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        if(player.leaderCardsProperty().size() != 4) // Do not load the leader cards if the player has not chosen what to discard yet
            for(LeaderCard card : player.leaderCardsProperty().keySet()) {
                addLeader(card, player.isLeaderCardActive(card));
            }

        player.leaderCardsProperty().addListener((MapChangeListener<? super LeaderCard, ? super Boolean>) change -> {
            if (change.wasAdded() && !change.wasRemoved()) {
                addLeader(change.getKey(), change.getValueAdded());
            } else if (change.wasRemoved() && change.wasAdded()) {
                updateLeaders(change.getKey(), change.getValueRemoved());
            } else removeLeader(change.getKey());
        });
    }

    private void removeLeader(LeaderCard cardToRemove) {
        Platform.runLater(() -> {
            leaderDisplay.getChildren().remove(leadersAndWidgets.get(cardToRemove));
            leadersAndWidgets.remove(cardToRemove);
        });
    }

    private void addLeader(LeaderCard newLeader, boolean active) {
        Platform.runLater(() -> {
            LeaderCardWidget newWidget = new LeaderCardWidget(newLeader);

            newWidget.setScaleX(0.75);
            newWidget.setScaleY(0.75);

            leaderDisplay.getChildren().add(new Group(newWidget));
            leadersAndWidgets.put(newLeader, newWidget);

            if(active)
                newWidget.getStyleClass().add("leader-active");
        });
    }

    private void updateLeaders(LeaderCard modifiedCard, boolean previousValue) {
        Platform.runLater(() -> {
            if (!previousValue) {
                leadersAndWidgets.get(modifiedCard).getStyleClass().add("leader-active");
            }
            else leadersAndWidgets.get(modifiedCard).getStyleClass().remove("leader-active");
        });
    }

}
