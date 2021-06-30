package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Widget that represents the resources drawn by a player from the market, waiting to be stored.
 */

public class MarketResultsWidget extends VBox {
    @FXML
    public FlowPane marketResultsDisplay;

    private final PlayerBoardWidget playerBoard;

    /**
     * Creates a new widget for the given player board of a player.
     * @param playerBoard the player board of the player that has drawn from the market
     */
    public MarketResultsWidget(PlayerBoardWidget playerBoard) {
        this.playerBoard = playerBoard;

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        for (int i = 0; i < 4; i++) {
            ImageView img = new ImageView();
            BorderPane imgWrapper = new BorderPane(img);
            imgWrapper.setPrefWidth(60);
            imgWrapper.setPrefHeight(60);
            imgWrapper.getStyleClass().add("resource-box");
            if (i != 3)
                imgWrapper.getStyleClass().add("margin-right");
            marketResultsDisplay.getChildren().add(imgWrapper);
        }

        updateMarketResults(playerBoard.getPlayer().getDeposit().marketResultProperty());
        playerBoard.getPlayer().getDeposit().marketResultProperty().addListener((ListChangeListener<Resource>) change -> updateMarketResults(change.getList().stream()
                .map(resToCast -> (Resource) resToCast).collect(Collectors.toList())));
    }

    /**
     * Updates the widget with the new drawn resources and sets the drag listener.
     * @param resources the list of resources drawn by a player
     */
    private void updateMarketResults(List<Resource> resources) {
        for (int i = 0; i < 4; i++) {
            ImageView imageView = (ImageView) ((BorderPane) marketResultsDisplay.getChildren().get(i)).getCenter();
            if (i < resources.size()) {
                imageView.setImage(GUIUtils.getResourceImage(resources.get(i), 50, 50));
                int finalI = i;
                imageView.setOnDragDetected(mouseEvent -> {
                    if (playerBoard.getPlayer().isLocalPlayer()) {
                        playerBoard.getDepositWidget().setDropAllowed(true);
                        playerBoard.getDepositWidget().setOnDragDroppedHandler(storeMarketResultHandler());

                        Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);

                        ClipboardContent content = new ClipboardContent();
                        content.putString("marketResult" + (finalI + 1));
                        content.putImage(imageView.getImage());
                        db.setContent(content);

                        mouseEvent.consume();
                    }
                });
                imageView.setOnDragDone(dragEvent -> playerBoard.getDepositWidget().setDropAllowed(false));
            } else {
                imageView.setImage(null);
            }
        }
    }

    /**
     * Constructs a consumer function for the deposit when the player has started
     * a drag and drop action from a resource in this widget.
     * @return a consumer function of drag event
     */
    private Consumer<DragEvent> storeMarketResultHandler() {
        return dragEvent -> {
            Dragboard db = dragEvent.getDragboard();

            boolean success = true;
            int resourceIndex = -1;
            int rowIndex = -1;
            try {
                resourceIndex = Integer.parseInt(db.getString().replace("marketResult", ""));

                rowIndex = DepositWidget.getRowId(dragEvent.getGestureTarget()) + 1;
            } catch (Exception e) {
                success = false;
            }

            if (success) {
                GUI.instance().getActionSender().storeMarketResult(resourceIndex, rowIndex);
            }

            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        };
    }
}
