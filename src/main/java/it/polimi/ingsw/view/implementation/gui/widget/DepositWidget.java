package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.function.Consumer;

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
    public DepositWidget(MockPlayer player) {
        this.player = player;
        this.dropAllowed = false;

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        player.getDeposit().depositProperty().addListener((ListChangeListener<? super List<Resource>>) change -> {
            while(change.next()) {
                int index = change.getFrom();
                List<Resource> newRow = change.getAddedSubList().get(0);
                updateRow(index, newRow);
            }
        });

        topRow.setOnDragDropped(dragEvent -> {
            if(dropAllowed) {
                dragDroppedHandler.accept(dragEvent);
            }
        });
        middleRow.setOnDragDropped(dragEvent -> {
            if(dropAllowed) {
                dragDroppedHandler.accept(dragEvent);
            }
        });
        bottomRow.setOnDragDropped(dragEvent -> {
            if(dropAllowed) {
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

        topRow.getChildren().add(buildEmptyImage());
        for(int i = 0; i < 2; i++) {
            middleRow.getChildren().add(buildEmptyImage());
        }
        for(int i = 0; i < 3; i++) {
            bottomRow.getChildren().add(buildEmptyImage());
        }

        List<List<Resource>> rows = player.getDeposit().getAllRows();
        for(int i = 0; i < 3; i++) {
            updateRow(i, rows.get(i));
        }
    }

    private void updateRow(int index, List<Resource> resources) {
        Platform.runLater(() -> {
            Image img = resources.size() > 0 ? GUIUtils.getResourceImage(resources.get(0), 39.0, 33.0) : null;
            switch (index) {
                case 0 -> {
                    ImageView view = ((ImageView) topRow.getChildren().get(0));
                    if(resources.size() > 0)
                        view.setImage(img);
                    else
                        view.setImage(null);
                }
                case 1 -> {
                    for(int i = 0; i < 2; i++) {
                        ImageView view = ((ImageView) middleRow.getChildren().get(i));
                        if(i < resources.size())
                            view.setImage(img);
                        else
                            view.setImage(null);
                    }
                }
                case 2 -> {
                    for(int i = 0; i < 3; i++) {
                        ImageView view = ((ImageView) bottomRow.getChildren().get(i));
                        if(i < resources.size())
                            view.setImage(img);
                        else
                            view.setImage(null);
                    }
                }
            }
        });
    }

    private ImageView buildEmptyImage() {
        ImageView img = new ImageView();
        img.setFitHeight(39);
        img.setFitHeight(33);
        return img;
    }

    public void setDropAllowed(boolean dropAllowed) {
        this.dropAllowed = dropAllowed;
    }

    public void setOnDragDroppedHandler(Consumer<DragEvent> eventHandler) {
        this.dragDroppedHandler = eventHandler;
    }

    public static int getRowId(Object node) throws IllegalArgumentException {
        if(!(node instanceof  Node))
            throw new IllegalArgumentException();
        int rowIndex;
        String id = ((Node) node).getId();
        switch (id) {
            case "topRow" -> rowIndex = 0;
            case "middleRow" -> rowIndex = 1;
            case "bottomRow" -> rowIndex = 2;
            default -> throw new IllegalArgumentException();
        }
        return rowIndex;
    }
}
