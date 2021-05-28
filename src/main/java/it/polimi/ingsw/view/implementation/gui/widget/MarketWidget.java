package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MarketWidget extends StackPane {
    @FXML
    private GridPane marketDisplay;
    @FXML
    private VBox exchangeModalContainer;
    @FXML
    private FlowPane exchangeModal;

    private final ImageView[][] gridPaneContent;
    private final HashMap<ImageView, Resource> resourceExchange;
    public MarketWidget() {
        this.gridPaneContent = new ImageView[3][5];
        this.resourceExchange = new HashMap<>();
        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        initializeGrid();

        ObservableList<List<Resource>> grid = GUI.instance().getModel().getMarket().gridProperty();
        ObjectProperty<Resource> slideResource = GUI.instance().getModel().getMarket().slideResourceProperty();

        for(int i = 0; i < grid.size(); i++) {
            updateRow(i, grid.get(i));
        }
        updateSlideResource(slideResource.get());

        grid.addListener((ListChangeListener<? super List<Resource>>) change -> {
            while(change.next()) {
                int index = change.getFrom();
                List<Resource> newRow = change.getAddedSubList().get(0);
                updateRow(index, newRow);
            }
        });
        slideResource.addListener((change, oldValue, newValue) -> updateSlideResource(newValue));
    }

    private void initializeGrid() {
        // Handler to accept drag events
        EventHandler<DragEvent> dragAccept = event -> {
            if (event.getGestureSource() instanceof ImageView &&
                    event.getDragboard().hasImage() && GUI.instance().isOwnTurn()) {
                event.acceptTransferModes(TransferMode.ANY);
            }

            event.consume();
        };

        // Populating the grid
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 6; j++) {
                if(j == 0 && i != 4 || i == 0 && j != 5) { // Cell of the slide
                    if(i != 3) {
                        BorderPane slideCell = new BorderPane();
                        slideCell.setPrefHeight(100);
                        slideCell.setPrefWidth(100);
                        slideCell.getStyleClass().add("slide");
                        marketDisplay.add(slideCell, j, i, 1, 1);
                    }
                } else if(j == 5 || i == 4) { // Cell of the margin
                    BorderPane marginCell = new BorderPane();
                    marginCell.setPrefHeight(100);
                    marginCell.setPrefWidth(100);
                    marginCell.getStyleClass().add("margin");
                    marketDisplay.add(marginCell, j, i, 1, 1);

                    // Cells of the margin that will accept drag events
                    if(i == 4 && j != 0 && j != 5) {
                        marginCell.setId("" + j);
                        marginCell.setCenter(new Label("^"));
                        marginCell.setOnDragOver(dragAccept);
                        marginCell.setOnDragDropped(dragDroppedHandler());
                    } else if(j == 5 && i != 0 && i != 4) {
                        marginCell.setId("" + (4 + i));
                        marginCell.setCenter(new Label("<"));
                        marginCell.setOnDragOver(dragAccept);
                        marginCell.setOnDragDropped(dragDroppedHandler());
                    }
                } else { // Cell of the main resource grid
                    ImageView emptyImg = new ImageView();
                    BorderPane imgWrapper = new BorderPane(emptyImg);
                    imgWrapper.getStyleClass().add("table");
                    marketDisplay.add(imgWrapper, j, i, 1, 1);
                    gridPaneContent[i - 1][j] = emptyImg;
                }
            }
        }

        // Populating the slide resource cell
        ImageView emptyImg = new ImageView();

        // Handler to start drag events from the slide resource
        emptyImg.setOnDragDetected(mouseEvent -> {
            if(GUI.instance().isOwnTurn()) {
                Dragboard db = emptyImg.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putImage(emptyImg.getImage());
                db.setContent(content);

                mouseEvent.consume();
            }
        });

        BorderPane imgWrapper = new BorderPane(emptyImg);
        imgWrapper.getStyleClass().add("slide");
        marketDisplay.add(imgWrapper, 0, 3, 1, 1);
        gridPaneContent[2][0] = emptyImg;
    }

    private void updateRow(int index, List<Resource> row) {
        Platform.runLater(() -> {
            for(int i = 1; i < 5; i++) {
                gridPaneContent[index][i].setImage(GUIUtils.getResourceImage(row.get(i - 1), 80, 80));
            }
        });
    }

    private void updateSlideResource(Resource resource) {
        Platform.runLater(() -> gridPaneContent[2][0].setImage(GUIUtils.getResourceImage(resource, 80, 80)));
    }

    private EventHandler<DragEvent> dragDroppedHandler() {
        return event -> {
            Node target = (Node) event.getGestureTarget();
            int marketIndex = Integer.parseInt(target.getId());

            List<Resource> availableToExchange = GUI.instance().getModel().getLocalPlayer().getExchangeAbility();
            if(availableToExchange.size() == 0) {
                GUI.instance().getActionSender().draw(marketIndex, availableToExchange);
            } else {
                int whiteResourcesCount = GUI.instance().getModel().getMarket().countWhiteResources(marketIndex);
                openExchangeModal(marketIndex, whiteResourcesCount, availableToExchange);
            }

            event.setDropCompleted(true);

            event.consume();
        };
    }

    private void openExchangeModal(int marketIndex, int whiteResourcesCount, List<Resource> availableToExchange) {
        Button confirmButton = new Button("Confirm");
        confirmButton.setDisable(true);
        confirmButton.setOnAction(actionEvent -> {
            confirmButton.setDisable(true);
            GUI.instance().getActionSender().draw(marketIndex, new ArrayList<>(resourceExchange.values()));
            exchangeModalContainer.setVisible(false);
        });
        HBox buttonBox = new HBox(confirmButton);
        buttonBox.setAlignment(Pos.CENTER);

        HBox whiteResources = new HBox();
        whiteResources.setSpacing(20);
        whiteResources.setAlignment(Pos.CENTER);

        EventHandler<DragEvent> dragOverHandler = dragEvent -> {
            if (dragEvent.getGestureSource() instanceof ImageView &&
                    dragEvent.getDragboard().hasString()) {
                dragEvent.acceptTransferModes(TransferMode.ANY);
            }

            dragEvent.consume();
        };

        for(int i = 0; i < whiteResourcesCount; i++) {
            ImageView imageView = new ImageView(GUIUtils.getResourceImage(null, 80, 80));

            resourceExchange.put(imageView, null);

            imageView.setOnDragOver(dragOverHandler);
            imageView.setOnDragDropped(dragEvent -> {
                Dragboard db = dragEvent.getDragboard();

                Resource resource = null;
                boolean success = true;
                try {
                    resource = Resource.valueOf(db.getString());
                } catch (IllegalArgumentException e) {
                    success = false;
                }

                if(success) {
                    imageView.setImage(GUIUtils.getResourceImage(resource, 80, 80));
                    resourceExchange.put(imageView, resource);

                    boolean done  = true;
                    for(Resource res : resourceExchange.values()) {
                        if(res == null) {
                            done = false;
                            break;
                        }
                    }
                    confirmButton.setDisable(!done);
                }

                dragEvent.setDropCompleted(success);
                dragEvent.consume();
            });

            whiteResources.getChildren().add(imageView);
        }

        HBox availableResources = new HBox();
        availableResources.setSpacing(20);
        availableResources.setAlignment(Pos.CENTER);
        for (Resource res : availableToExchange) {
            ImageView imageView = new ImageView(GUIUtils.getResourceImage(res, 80, 80));

            imageView.setOnDragDetected(mouseEvent -> {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);

                db.setContent(GUIUtils.getClipboardForResource(res, imageView));

                mouseEvent.consume();
            });

            availableResources.getChildren().add(imageView);
        }

        exchangeModal.getChildren().setAll(new Label("Drop the resources that you want to \nexchange over the slots:"),
                whiteResources, availableResources, buttonBox);
        exchangeModalContainer.setVisible(true);
    }

    @FXML
    private void goToPlayerBoard() {
        GUI.instance().getScene().setRoot(new PlayerBoardWidget(GUI.instance().getModel().getLocalPlayer()));
    }
}
