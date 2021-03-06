package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.messages.BuyPlayerActionEvent;
import it.polimi.ingsw.view.messages.production.DevelopmentProduction;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Widget that shows the stacks of development cards owned by the player.
 */

public class DevelopmentSlotsWidget extends StackPane {
    private final List<GridPane> layerGrids;
    private final Map<GridPane, List<VBox>> gridSlots;
    private EventHandler<? super DragEvent> dragOverListener;

    private final PlayerBoardWidget playerBoard;
    private final MockPlayer player;

    /**
     * Creates a new DevelopmentSlotsWidget for the given player board.
     * @param playerBoard the board of the player that owns the stacks of cards
     */

    public DevelopmentSlotsWidget(PlayerBoardWidget playerBoard) {
        this.playerBoard = playerBoard;
        layerGrids = new ArrayList<>();
        gridSlots = new HashMap<>();
        this.player = playerBoard.getPlayer();

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    public void initialize() {
        dragOverListener = event -> {
            if (event.getGestureSource() instanceof BorderPane &&
                    event.getDragboard().hasImage() && GUI.instance().isOwnTurn() && player.isLocalPlayer()) {
                event.acceptTransferModes(TransferMode.ANY);
            }

            event.consume();
        };

        Platform.runLater(() -> {
            layerGrids.add(new GridPane());
            initGrid(layerGrids.get(0));
            getChildren().add(layerGrids.get(0));
        });

        List<ObservableList<DevelopmentCard>> stacks = player.getPlayerBoard().getDevelopmentCards();
        for (int i = 0; i < stacks.size(); i++) {
            for (DevelopmentCard card : stacks.get(i)) {
                pushCard(card, i);
            }
        }

        for (int i = 0; i < stacks.size(); i++) {
            int finalI = i;
            stacks.get(i).addListener((ListChangeListener<? super DevelopmentCard>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (DevelopmentCard addedItem : change.getAddedSubList()) {
                            pushCard(addedItem, finalI);
                        }
                    }
                }
            });
        }
    }

    /**
     * Inserts a new development card widget in the given slot, then sets the click event to open the production.
     * @param addedItem the bought development card
     * @param slotIndex the index of the slot where to put the new card
     */
    private void pushCard(DevelopmentCard addedItem, int slotIndex) {
        Platform.runLater(() -> {
            GridPane chosenLayer = null;
            for (GridPane grid : layerGrids) {
                if (checkSlotEmpty(grid, slotIndex) == null) {
                    chosenLayer = grid;
                    break;
                }
            }
            if (chosenLayer == null) {
                Node prevCard = checkSlotEmpty(layerGrids.get(layerGrids.size()-1), slotIndex);
                prevCard.setOnMouseClicked(null);
                layerGrids.add(new GridPane());
                initGrid(layerGrids.get(layerGrids.size() - 1));
                layerGrids.get(layerGrids.size() - 1).setPickOnBounds(false);
                layerGrids.get(layerGrids.size() - 1).setTranslateY(-40.0 * (layerGrids.size() - 1));
                getChildren().add(layerGrids.get(layerGrids.size() - 1));
                chosenLayer = layerGrids.get(layerGrids.size() - 1);
            }
            DevelopmentCardWidget card = new DevelopmentCardWidget(addedItem);

            List<Resource> input = new ArrayList<>();
            List<Resource> output = new ArrayList<>();
            addedItem.getProductionInput().forEach((res, quantity) -> {
                for (int i = 0; i < quantity; i++) {
                    input.add(res);
                }
            });
            addedItem.getProductionOutput().forEach((res, quantity) -> {
                for (int i = 0; i < quantity; i++) {
                    output.add(res);
                }
            });
            card.setOnMouseClicked(mouseEvent ->
                    playerBoard.openProductionModal(input, output, DevelopmentProduction.class, addedItem));

            card.setScaleX(0.8);
            card.setScaleY(0.8);
            gridSlots.get(chosenLayer).get(slotIndex).getChildren().add(card);
        });
    }

    /**
     * Checks if a slot is empty or has already a card in it.
     * @param pane the grid pane where to check if there is a card or not
     * @param slotIndex the index of the slot
     * @return the DevelopmentCardWidget that is in the slot, or null if the slot is empty
     */
    private Node checkSlotEmpty(GridPane pane, int slotIndex) {
        Node result = null;
        ObservableList<Node> childrens = pane.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == 0 && GridPane.getColumnIndex(node) == slotIndex && node instanceof VBox) {
                if (((VBox) node).getChildren().size() > 0) result = ((VBox) node).getChildren().get(0);
                break;
            }
        }
        return result;
    }

    /**
     * Creates the placeholders for DevelopmentCardWidgets for the new grid layer.
     * @param grid the new grid layer
     */
    private void initGrid(GridPane grid) {
        List<VBox> slots = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            slots.add(new VBox());
            if(grid != layerGrids.get(0)) slots.get(i).setPickOnBounds(false);
            slots.get(i).setPrefHeight(294.0);
            slots.get(i).setPrefWidth(195.0);
            GridPane.setRowIndex(slots.get(i), 0);
            GridPane.setColumnIndex(slots.get(i), i);
            slots.get(i).setOnDragOver(dragOverListener);
            slots.get(i).setOnDragDropped(buildOnDragDropped(i));
        }

        gridSlots.put(grid, slots);
        grid.getChildren().addAll(slots);

    }

    /**
     * Creates the callback function for when the player wants to place a new card on the new bought card
     * @param slotIndex the slot index
     * @return a callback function for a drop event
     */
    private EventHandler<? super DragEvent> buildOnDragDropped(int slotIndex) {
        return event -> {
            boolean success = false;
            BorderPane pane;
            Group group;
            DevelopmentCardWidget developmentCardWidget;
            DevelopmentCard card = null;
            if (event.getGestureSource() instanceof BorderPane && GUI.instance().isOwnTurn()) {
                if (player.isLocalPlayer()) {
                    success = true;
                    pane = (BorderPane) event.getGestureSource();
                    group = (Group) pane.getChildren().get(0);
                    developmentCardWidget = (DevelopmentCardWidget) group.getChildren().get(0);
                    card = developmentCardWidget.getDevelopmentCard();
                }
            }

            if (success) {
                GUI.instance().getClient().send(new BuyPlayerActionEvent(card.getUuid(), slotIndex + 1));
            }
            event.setDropCompleted(success);

            event.consume();
        };
    }
}
