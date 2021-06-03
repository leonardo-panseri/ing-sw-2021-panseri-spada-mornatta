package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeckWidget extends StackPane {

    private final List<GridPane> layerGrids;

    public DeckWidget() {
        layerGrids = new ArrayList<>();
        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        List<HashMap<CardColor, ObservableList<DevelopmentCard>>> developmentDeck = GUI.instance().getModel().getDevelopmentDeck();
        for (int i=0; i<4; i++) {
            layerGrids.add(new GridPane());
            VBox vBox = new VBox();
            vBox.getChildren().add(layerGrids.get(i));
            vBox.setAlignment(Pos.CENTER);
            HBox hBox = new HBox();
            hBox.getChildren().add(vBox);
            hBox.setAlignment(Pos.CENTER);
            hBox.setTranslateX(i*10);
            getChildren().add(hBox);
        }

        for (int i=0; i<4; i++) {
            for (CardColor color : CardColor.values()) {
                if(developmentDeck.get(0).get(color).size() <= i)
                    continue;
                DevelopmentCardWidget card = new DevelopmentCardWidget(developmentDeck.get(0).get(color).get(developmentDeck.get(0).get(color).size()-i-1));
                BorderPane pane = createPane(color, card);
                GridPane.setRowIndex(pane, 2);
                layerGrids.get(3-i).getChildren().add(pane);
            }
            for (CardColor color : CardColor.values()) {
                if(developmentDeck.get(1).get(color).size() <= i)
                    continue;
                DevelopmentCardWidget card = new DevelopmentCardWidget(developmentDeck.get(1).get(color).get(developmentDeck.get(1).get(color).size()-i-1));
                BorderPane pane = createPane(color, card);
                GridPane.setRowIndex(pane, 1);
                layerGrids.get(3-i).getChildren().add(pane);
            }
            for (CardColor color : CardColor.values()) {
                if(developmentDeck.get(2).get(color).size() <= i)
                    continue;
                DevelopmentCardWidget card = new DevelopmentCardWidget(developmentDeck.get(2).get(color).get(developmentDeck.get(2).get(color).size()-i-1));
                BorderPane pane = createPane(color, card);
                GridPane.setRowIndex(pane, 0);
                layerGrids.get(3-i).getChildren().add(pane);
            }
        }

        Button goBackButton = new Button("Back");
        goBackButton.setOnAction(event -> goToPlayerBoard());
        AnchorPane container = new AnchorPane();
        AnchorPane.setLeftAnchor(goBackButton, 10.0);
        AnchorPane.setTopAnchor(goBackButton, 10.0);
        container.getChildren().add(goBackButton);
        container.setPickOnBounds(false);
        getChildren().add(container);

        for (HashMap<CardColor, ObservableList<DevelopmentCard>> map : developmentDeck) {
            for (ObservableList<DevelopmentCard> stack : map.values()) {
                stack.addListener((ListChangeListener<? super DevelopmentCard>) change -> {
                    change.next();
                    if (change.wasRemoved()) {
                        for (DevelopmentCard remItem : change.getRemoved()) {
                            removeCards(remItem);
                        }
                    }
                });
            }
        }

        ObservableList<Node> children = layerGrids.get(3).getChildren();
        for(Node node : children) {
            setDraggableDevCard(node);
        }
    }

    private BorderPane createPane(CardColor color, DevelopmentCardWidget card) {
        double SCALE_FACTOR = 0.65;
        card.setScaleX(SCALE_FACTOR);
        card.setScaleY(SCALE_FACTOR);
        Group group = new Group(card);
        BorderPane pane = new BorderPane(group);
        pane.getStyleClass().add("table");
        GridPane.setColumnIndex(pane, CardColor.valueOf(color.toString()).ordinal());
        return pane;
    }

    private void removeCards(DevelopmentCard removedCard) {
        Platform.runLater(() -> {
            int modifiedLayerIndex = GUI.instance().getModel().getDevelopmentDeck().get(removedCard.getLevel()).get(removedCard.getColor()).size()-1;
            GridPane modifiedLayer = layerGrids.get(modifiedLayerIndex);
            ObservableList<Node> children = modifiedLayer.getChildren();
            for(Node node : children) {
                if(node instanceof BorderPane && GridPane.getRowIndex(node) == 3-removedCard.getLevel() && GridPane.getColumnIndex(node) == CardColor.valueOf(removedCard.getColor().toString()).ordinal()) {
                    BorderPane pane = new BorderPane(node);
                    modifiedLayer.getChildren().remove(pane);
                    break;
                }
            }
            if (modifiedLayerIndex > 0) updateDraggable(modifiedLayerIndex-1, removedCard.getColor(), removedCard.getLevel());
        });
    }

    private void updateDraggable(int layerToBeUpdated, CardColor color, int level) {
        ObservableList<Node> children = layerGrids.get(layerToBeUpdated).getChildren();
        for(Node node : children) {
            if(node instanceof BorderPane && GridPane.getRowIndex(node) == 3-level && GridPane.getColumnIndex(node) == CardColor.valueOf(color.toString()).ordinal()) {
                setDraggableDevCard(node);
                break;
            }
        }
    }

    private void setDraggableDevCard(Node node) {
        BorderPane pane = (BorderPane) node;
        Group group = (Group) pane.getChildren().get(0);
        DevelopmentCardWidget developmentCardWidget = (DevelopmentCardWidget) group.getChildren().get(0);
        node.setOnDragDetected(mouseEvent -> {
            if(GUI.instance().isOwnTurn()) {
                Dragboard db = node.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                WritableImage snapshot = developmentCardWidget.snapshot(null, null);
                content.putImage(snapshot);
                db.setContent(content);

                GUI.instance().getScene().setRoot(new PlayerBoardWidget(GUI.instance().getModel().getLocalPlayer()));

                mouseEvent.consume();
            }
        });
    }

    @FXML
    private void goToPlayerBoard() {
        GUI.instance().getScene().setRoot(new PlayerBoardWidget(GUI.instance().getModel().getLocalPlayer()));
    }
}

