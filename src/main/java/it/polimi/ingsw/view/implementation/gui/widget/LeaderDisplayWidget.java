package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.messages.ActivateLeaderPlayerActionEvent;
import it.polimi.ingsw.view.messages.DiscardLeaderPlayerActionEvent;
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

/**
 * Widget that shows the leader cards possessed by a player.
 */

public class LeaderDisplayWidget extends VBox {
    @FXML
    public VBox leaderDisplay;

    private final PlayerBoardWidget playerBoard;
    private final MockPlayer player;
    private final Map<LeaderCard, Group> leadersAndWidgets;

    /**
     * Creates a new leader display widget for the given player board.
     * @param playerBoard the player board of the player that owns the leader cards
     */
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

    /**
     * Removes a leader card widget after the player discarded the relative leader card
     * @param cardToRemove the leader card to remove
     */
    private void removeLeader(LeaderCard cardToRemove) {
        Platform.runLater(() -> {
            leaderDisplay.getChildren().remove(leadersAndWidgets.get(cardToRemove));
            leadersAndWidgets.remove(cardToRemove);
        });
    }

    /**
     * Creates a new leader card widget and adds it to the leader display
     * @param newLeader the new leader card to be added
     * @param active true if the card is active
     */
    private void addLeader(LeaderCard newLeader, boolean active) {
        Platform.runLater(() -> {
            LeaderCardWidget newWidget = new LeaderCardWidget(newLeader);

            newWidget.setScaleX(0.75);
            newWidget.setScaleY(0.75);

            if (!active && !player.isLocalPlayer())
                newWidget.flipCard();

            Group leaderWrapper = new Group(newWidget);
            leaderDisplay.getChildren().add(leaderWrapper);
            leadersAndWidgets.put(newLeader, leaderWrapper);

            if (active)
                newWidget.getStyleClass().add("leader-active");
            else {
                if (player.isLocalPlayer()) {
                    ContextMenu contextMenu = buildContextMenu(newWidget);
                    newWidget.setOnMouseClicked(event -> {
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

    /**
     * Sets the visual property of a leader card when it is activated. If the card has a production power,
     * sets the click handler for the use of the power.
     * @param modifiedCard the leader card that has been activated
     * @param previousValue false if the card was not active
     */
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

    /**
     * Builds a context menu for a leader card widget, to permit the activation of the leader card
     * or to let the player discard it.
     * @param cardWidget the leader card widget that where to put the new context menu
     * @return the context menu with the two options
     */
    private ContextMenu buildContextMenu(LeaderCardWidget cardWidget) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Activate Leader");
        MenuItem menuItem2 = new MenuItem("Discard Leader");

        menuItem1.setOnAction((event) -> GUI.instance().getClient().send(new ActivateLeaderPlayerActionEvent(cardWidget.getLeaderCard().getUuid())));
        menuItem2.setOnAction((event -> GUI.instance().getClient().send(new DiscardLeaderPlayerActionEvent(cardWidget.getLeaderCard().getUuid()))));

        contextMenu.getItems().addAll(menuItem1, menuItem2);
        return contextMenu;
    }
}
