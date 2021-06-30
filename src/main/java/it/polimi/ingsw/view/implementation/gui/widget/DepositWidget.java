package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.function.Consumer;

/**
 * Widget that represents a deposit.
 */

public class DepositWidget extends AnchorPane {
    @FXML
    private HBox topRow;
    @FXML
    private HBox middleRow;
    @FXML
    private HBox bottomRow;

    private final MockPlayer player;
    private boolean dropAllowed;
    private Consumer<DragEvent> dragDroppedHandler;

    /**
     * Creates a new deposit widget for the given player.
     * @param player the player that owns the deposit
     */
    public DepositWidget(MockPlayer player) {
        this.player = player;
        this.dropAllowed = false;

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        player.getDeposit().depositProperty().addListener((ListChangeListener<? super List<Resource>>) change -> {
            while (change.next()) {
                int index = change.getFrom();
                for (List<Resource> newRow : change.getAddedSubList())
                    updateRow(index, newRow);
            }
        });

        topRow.setOnDragDropped(dragEvent -> {
            if (dropAllowed) {
                dragDroppedHandler.accept(dragEvent);
            }
        });
        middleRow.setOnDragDropped(dragEvent -> {
            if (dropAllowed) {
                dragDroppedHandler.accept(dragEvent);
            }
        });
        bottomRow.setOnDragDropped(dragEvent -> {
            if (dropAllowed) {
                dragDroppedHandler.accept(dragEvent);
            }
        });

        EventHandler<? super DragEvent> dragOverListener = event -> {
            if (event.getGestureSource() instanceof ImageView &&
                    event.getDragboard().hasString() && dropAllowed) {
                event.acceptTransferModes(TransferMode.ANY);
            }

            event.consume();
        };
        topRow.setOnDragOver(dragOverListener);
        middleRow.setOnDragOver(dragOverListener);
        bottomRow.setOnDragOver(dragOverListener);

        topRow.getChildren().add(buildEmptyImage("topRow"));
        for (int i = 0; i < 2; i++) {
            middleRow.getChildren().add(buildEmptyImage("middleRow"));
        }
        for (int i = 0; i < 3; i++) {
            bottomRow.getChildren().add(buildEmptyImage("bottomRow"));
        }

        List<List<Resource>> rows = player.getDeposit().getAllRows();
        for (int i = 0; i < 3; i++) {
            updateRow(i, rows.get(i));
        }
    }

    /**
     * Updates the deposit with the modified row provided in input.
     * @param index the index of the modified row
     * @param resources the modified row of resources
     */
    private void updateRow(int index, List<Resource> resources) {
        Platform.runLater(() -> {
            Image img = resources.size() > 0 ? GUIUtils.getResourceImage(resources.get(0), 39.0, 33.0) : null;
            switch (index) {
                case 0 -> {
                    ImageView view = ((ImageView) topRow.getChildren().get(0));
                    if (resources.size() > 0)
                        view.setImage(img);
                    else
                        view.setImage(null);
                }
                case 1 -> {
                    for (int i = 0; i < 2; i++) {
                        ImageView view = ((ImageView) middleRow.getChildren().get(i));
                        if (i < resources.size())
                            view.setImage(img);
                        else
                            view.setImage(null);
                    }
                }
                case 2 -> {
                    for (int i = 0; i < 3; i++) {
                        ImageView view = ((ImageView) bottomRow.getChildren().get(i));
                        if (i < resources.size())
                            view.setImage(img);
                        else
                            view.setImage(null);
                    }
                }
            }
        });
    }

    /**
     * Builds an empty image view for a specified row that later will contain a resource.
     * @param rowId the row where to build an empty image view
     * @return an empty image view
     */
    private ImageView buildEmptyImage(String rowId) {
        ImageView img = new ImageView();
        img.setFitHeight(39);
        img.setFitHeight(33);

        img.setOnDragDetected(mouseEvent -> {
            if (img.getImage() != null && player.isLocalPlayer() && GUI.instance().getGameState() == GameState.PLAYING) {
                Dragboard db = img.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString(rowId);
                content.putImage(img.getImage());
                db.setContent(content);

                setDropAllowed(true);
                setOnDragDroppedHandler(moveResourceHandler());

                mouseEvent.consume();
            }
        });
        img.setOnDragDone(dragEvent -> setDropAllowed(false));

        return img;
    }

    /**
     * Consumer function called after a drag and drop on the deposit. It handles the various player actions that move resources
     * from one storage to another.
     * @return a consumer of the drag event that executes this function
     */
    private Consumer<DragEvent> moveResourceHandler() {
        return dragEvent -> {
            boolean success = true;
            int rowFromIndex = -1;
            int rowToIndex = -1;
            try {
                rowFromIndex = getRowId(((Node) dragEvent.getGestureSource()).getParent()) + 1;
                rowToIndex = getRowId(dragEvent.getGestureTarget()) + 1;
            } catch (Exception e) {
                success = false;
            }

            if (success) {
                GUI.instance().getActionSender().move(rowFromIndex, rowToIndex);
            }

            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        };
    }

    /**
     * Sets if drop is allowed in this deposit.
     * @param dropAllowed true if drop is allowed
     */
    public void setDropAllowed(boolean dropAllowed) {
        this.dropAllowed = dropAllowed;
    }

    /**
     * Sets the handler for drop events on this deposit.
     * @param eventHandler the consumer to be set as drop event callback function
     */
    public void setOnDragDroppedHandler(Consumer<DragEvent> eventHandler) {
        this.dragDroppedHandler = eventHandler;
    }

    /**
     * Gets the row id for the given resource node.
     * @param node the node containing the resource
     * @return the index of the resource
     * @throws IllegalArgumentException if the input is not a Node, or if the resource is not in any storage
     */
    public static int getRowId(Object node) throws IllegalArgumentException {
        if (!(node instanceof Node))
            throw new IllegalArgumentException();
        int rowIndex;
        String id = ((Node) node).getId();
        switch (id) {
            case "topRow" -> rowIndex = 0;
            case "middleRow" -> rowIndex = 1;
            case "bottomRow" -> rowIndex = 2;
            case "leaderDeposit1" -> rowIndex = 4;
            case "leaderDeposit2" -> rowIndex = 5;
            default -> throw new IllegalArgumentException();
        }
        return rowIndex;
    }
}
