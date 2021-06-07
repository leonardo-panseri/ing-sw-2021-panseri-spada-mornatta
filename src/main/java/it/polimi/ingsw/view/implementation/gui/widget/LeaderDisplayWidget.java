package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.messages.ActivateLeaderPlayerActionEvent;
import it.polimi.ingsw.view.messages.production.LeaderProduction;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LeaderDisplayWidget extends VBox {
    @FXML
    public VBox leaderDisplay;

    private final PlayerBoardWidget playerBoard;
    private final MockPlayer player;
    private final Map<LeaderCard, LeaderCardWidget> leadersAndWidgets;

    public LeaderDisplayWidget(PlayerBoardWidget playerBoard) {
        this.playerBoard = playerBoard;
        this.player = playerBoard.getPlayer();
        this.leadersAndWidgets = new HashMap<>();

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        if (player.leaderCardsProperty().size() != 4) // Do not load the leader cards if the player has not chosen what to discard yet
            for (LeaderCard card : player.leaderCardsProperty().keySet()) {
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

            if (!player.isLocalPlayer())
                newWidget.flipCard();

            leaderDisplay.getChildren().add(new Group(newWidget));
            leadersAndWidgets.put(newLeader, newWidget);

            if (active)
                newWidget.getStyleClass().add("leader-active");
            else {
                if (player.isLocalPlayer()) {
                    ContextMenu contextMenu = buildContextMenu(newWidget);
                    newWidget.setOnMousePressed(event -> {
                        if (!contextMenu.isShowing())
                            contextMenu.show(newWidget, event.getScreenX(), event.getScreenY());
                    });
                }
            }

            if (active && newLeader.getSpecialAbility().getType() == SpecialAbilityType.PRODUCTION) {
                newWidget.setOnMouseClicked(mouseEvent -> playerBoard.openProductionModal(
                        Collections.singletonList(newLeader.getSpecialAbility().getTargetResource()),
                        Arrays.asList(null, Resource.FAITH), LeaderProduction.class, newLeader));
            }
        });
    }

    private void updateLeaders(LeaderCard modifiedCard, boolean previousValue) {
        Platform.runLater(() -> {
            if (!previousValue) {
                leadersAndWidgets.get(modifiedCard).getStyleClass().add("leader-active");
                leadersAndWidgets.get(modifiedCard).setOnMouseClicked(null);

                if (modifiedCard.getSpecialAbility().getType() == SpecialAbilityType.PRODUCTION) {
                    leadersAndWidgets.get(modifiedCard).setOnMouseClicked(mouseEvent -> playerBoard.openProductionModal(
                            Collections.singletonList(modifiedCard.getSpecialAbility().getTargetResource()),
                            Arrays.asList(null, Resource.FAITH), LeaderProduction.class, modifiedCard));
                }
            }
        });
    }

    private ContextMenu buildContextMenu(LeaderCardWidget cardWidget) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Activate Leader");

        menuItem1.setOnAction((event) -> GUI.instance().getClient().send(new ActivateLeaderPlayerActionEvent(cardWidget.getLeaderCard().getUuid())));

        contextMenu.getItems().addAll(menuItem1);
        return contextMenu;
    }
}
