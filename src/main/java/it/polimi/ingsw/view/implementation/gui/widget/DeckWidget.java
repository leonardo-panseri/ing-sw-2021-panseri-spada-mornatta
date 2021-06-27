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
import java.util.Map;
import java.util.function.Consumer;

/**
 * Widget that represents the deck of development cards.
 */

public class DeckWidget extends StackPane {

    private final List<GridPane> layerGrids;
    private int loadedLevels = 0;

    /**
     * Creates a new deck widget.
     */
    public DeckWidget() {
        layerGrids = new ArrayList<>();
        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        List<HashMap<CardColor, ObservableList<DevelopmentCard>>> developmentDeck = GUI.instance().getModel().getDevelopmentDeck();
        for (int i = 0; i < 4; i++) {
            GridPane gridPane = new GridPane();

            for (int j = 0; j < 4; j++) {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / 4);
                gridPane.getColumnConstraints().add(colConst);
            }
            for (int j = 0; j < 3; j++) {
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPercentHeight(100.0 / 3);
                gridPane.getRowConstraints().add(rowConst);
            }

            layerGrids.add(gridPane);
            VBox vBox = new VBox();
            vBox.getChildren().add(layerGrids.get(i));
            vBox.setAlignment(Pos.CENTER);
            HBox hBox = new HBox();
            hBox.getChildren().add(vBox);
            hBox.setAlignment(Pos.CENTER);
            hBox.setTranslateX(i * 10);
            getChildren().add(hBox);
        }

        Consumer<LoadCards.LoadedCards> callback = result -> Platform.runLater(() -> {
            result.addToLayerGrids(layerGrids);
            loadedLevels++;

            if(loadedLevels == 3) {
                ObservableList<Node> children = layerGrids.get(3).getChildren();
                for (Node node : children) {
                    setDraggableDevCard(node);
                }
            }
        });
        for(int i = 0; i < 3; i++) {
            new Thread(new LoadCards(i, callback)).start();
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
    }

    /**
     * Callback function that removes the bought card from the widget.
     * @param removedCard the bought card
     */
    private void removeCards(DevelopmentCard removedCard) {
        Platform.runLater(() -> {
            int modifiedLayerIndex = GUI.instance().getModel().getDevelopmentDeck().get(removedCard.getLevel() - 1).get(removedCard.getColor()).size() - 1;
            GridPane modifiedLayer = layerGrids.get(modifiedLayerIndex);
            ObservableList<Node> children = modifiedLayer.getChildren();
            for (Node node : children) {
                if (node instanceof BorderPane && GridPane.getRowIndex(node) == 3 - removedCard.getLevel() && GridPane.getColumnIndex(node) == CardColor.valueOf(removedCard.getColor().toString()).ordinal()) {
                    BorderPane pane = new BorderPane(node);
                    modifiedLayer.getChildren().remove(pane);
                    break;
                }
            }
            if (modifiedLayerIndex > 0)
                updateDraggable(modifiedLayerIndex - 1, removedCard.getColor(), removedCard.getLevel());
        });
    }

    /**
     * Function that sets the drag function to the next card in the stack, when the previous one has been bought.
     * @param layerToBeUpdated the grid pane that contains the bought card
     * @param color the color of the bought card
     * @param level the level of the bought card
     */
    private void updateDraggable(int layerToBeUpdated, CardColor color, int level) {
        ObservableList<Node> children = layerGrids.get(layerToBeUpdated).getChildren();
        for (Node node : children) {
            if (node instanceof BorderPane && GridPane.getRowIndex(node) == 3 - level && GridPane.getColumnIndex(node) == CardColor.valueOf(color.toString()).ordinal()) {
                setDraggableDevCard(node);
                break;
            }
        }
    }

    /**
     * Function that sets the drag function to the given card.
     * @param node the node containing the card
     */
    private void setDraggableDevCard(Node node) {
        BorderPane pane = (BorderPane) node;
        Group group = (Group) pane.getChildren().get(0);
        DevelopmentCardWidget developmentCardWidget = (DevelopmentCardWidget) group.getChildren().get(0);
        node.setOnDragDetected(mouseEvent -> {
            if (GUI.instance().isOwnTurn()) {
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

/**
 * Runnable class to permit asynchronous loading of the development decks when loading this widget.
 */
class LoadCards implements Runnable {
    private static final double SCALE_FACTOR = 0.68;

    private final int level;
    private final LoadedCards result;
    private final Consumer<LoadedCards> callback;

    static class LoadedCards {
        private final Map<Integer, List<BorderPane>> result;

        LoadedCards() {
            this.result = new HashMap<>();

            result.put(0, new ArrayList<>());
            result.put(1, new ArrayList<>());
            result.put(2, new ArrayList<>());
            result.put(3, new ArrayList<>());
        }

        void add(int index, BorderPane pane) {
            result.get(index).add(pane);
        }

        void addToLayerGrids(List<GridPane> layerGrids) {
            for(int i = 0; i < 4; i++) {
                layerGrids.get(i).getChildren().addAll(result.get(i));
            }
        }
    }

    /**
     * Creates LoadCards runnable for the deck of the given level, using a callback function at the end.
     * @param level the level of the deck
     * @param callback the function to be called at the end of the execution
     */
    LoadCards(int level, Consumer<LoadedCards> callback) {
        this.level = level;
        this.result = new LoadedCards();
        this.callback = callback;
    }

    @Override
    public void run() {
        List<HashMap<CardColor, ObservableList<DevelopmentCard>>> developmentDeck = GUI.instance().getModel().getDevelopmentDeck();
        for (int i = 0; i < 4; i++) {
            for (CardColor color : CardColor.values()) {
                if (developmentDeck.get(level).get(color).size() <= i)
                    continue;
                DevelopmentCardWidget card = new DevelopmentCardWidget(developmentDeck.get(level).get(color).get(developmentDeck.get(level).get(color).size() - i - 1));
                BorderPane pane = createPane(color, card);
                GridPane.setRowIndex(pane, 2 - level);
                result.add(3-i, pane);
            }
        }

        callback.accept(result);
    }

    /**
     * Creates a BorderPane in the right position of the grid depending on the color of the card.
     * @param color the color of the card
     * @param card the card
     * @return a border pane containing the card widget
     */
    private BorderPane createPane(CardColor color, DevelopmentCardWidget card) {
        card.setScaleX(SCALE_FACTOR);
        card.setScaleY(SCALE_FACTOR);
        Group group = new Group(card);
        BorderPane pane = new BorderPane(group);
        pane.getStyleClass().add("table");
        GridPane.setColumnIndex(pane, CardColor.valueOf(color.toString()).ordinal());
        return pane;
    }
}
