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

            for (CardColor color : CardColor.values()) {
                DevelopmentCardWidget card = new DevelopmentCardWidget(developmentDeck.get(0).get(color).get(i));
                BorderPane pane = createPane(color, card);
                GridPane.setRowIndex(pane, 2);
                layerGrids.get(i).getChildren().add(pane);
            }
            for (CardColor color : CardColor.values()) {
                DevelopmentCardWidget card = new DevelopmentCardWidget(developmentDeck.get(1).get(color).get(i));
                BorderPane pane = createPane(color, card);
                GridPane.setRowIndex(pane, 1);
                layerGrids.get(i).getChildren().add(pane);
            }
            for (CardColor color : CardColor.values()) {
                DevelopmentCardWidget card = new DevelopmentCardWidget(developmentDeck.get(2).get(color).get(i));
                BorderPane pane = createPane(color, card);
                GridPane.setRowIndex(pane, 0);
                layerGrids.get(i).getChildren().add(pane);
            }

            hBox.setTranslateX(i*10);
            getChildren().add(hBox);
        }

        for (HashMap<CardColor, ObservableList<DevelopmentCard>> map : GUI.instance().getModel().getDevelopmentDeck()) {
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
            GridPane modifiedLayer = layerGrids.get(layerGrids.size() - 1);
            ObservableList<Node> childrens = modifiedLayer.getChildren();
            for(Node node : childrens) {
                if(node instanceof BorderPane && GridPane.getRowIndex(node) == 3-removedCard.getLevel() && GridPane.getColumnIndex(node) == CardColor.valueOf(removedCard.getColor().toString()).ordinal()) {
                    BorderPane pane = new BorderPane(node);
                    modifiedLayer.getChildren().remove(pane);
                    break;
                }
            }
        });
    }

}

