package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.messages.BuyPlayerActionEvent;
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

public class DevelopmentSlotsWidget extends StackPane {
    private final List<GridPane> layerGrids;
    private final Map<GridPane, List<VBox>> gridSlots;
    private final MockPlayer player;
    private EventHandler<? super DragEvent> dragOverListener;

    public DevelopmentSlotsWidget(MockPlayer player) {
        layerGrids = new ArrayList<>();
        gridSlots = new HashMap<>();
        this.player = player;

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

    private void pushCard(DevelopmentCard addedItem, int slotIndex) {
        Platform.runLater(() -> {
            if (checkSlotEmpty(layerGrids.get(layerGrids.size() - 1), slotIndex) != null) {
                layerGrids.add(new GridPane());
                initGrid(layerGrids.get(layerGrids.size() - 1));
                layerGrids.get(layerGrids.size() - 1).setTranslateY(-10.0);
                getChildren().add(layerGrids.get(layerGrids.size() - 1));
            }
            GridPane chosenLayer = layerGrids.get(layerGrids.size() - 1);
            DevelopmentCardWidget card = new DevelopmentCardWidget(addedItem);
            gridSlots.get(chosenLayer).get(slotIndex).getChildren().add(card);
        });
    }

    private Node checkSlotEmpty(GridPane pane, int slotIndex) {
        Node result = null;
        ObservableList<Node> childrens = pane.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == 0 && GridPane.getColumnIndex(node) == slotIndex && node instanceof VBox) {
                if(((VBox) node).getChildren().size() > 0) result = node;
                break;
            }
        }
        return result;
    }

    private void initGrid(GridPane grid) {
        List<VBox> slots = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            slots.add(new VBox());
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

    private EventHandler<? super DragEvent> buildOnDragDropped(int slotIndex) {
        return event -> {
            boolean success = false;
            BorderPane pane;
            Group group;
            DevelopmentCardWidget developmentCardWidget;
            DevelopmentCard card = null;
            if (event.getGestureTarget() instanceof BorderPane && GUI.instance().isOwnTurn()) {
                if(player.isLocalPlayer()){
                    success = true;
                    pane = (BorderPane) event.getGestureTarget();
                    group = (Group) pane.getChildren().get(0);
                    developmentCardWidget = (DevelopmentCardWidget) group.getChildren().get(0);
                    card = developmentCardWidget.getDevelopmentCard();
                }
            }

            if(success) {
                GUI.instance().getClient().send(new BuyPlayerActionEvent(card.getUuid(), slotIndex+1));
            }
            event.setDropCompleted(success);

            event.consume();
        };
    }
}
