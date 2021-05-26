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
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.List;

public class MarketWidget extends StackPane {
    @FXML
    private GridPane marketDisplay;

    private final ImageView[][] gridPaneContent;
    private BooleanProperty ownTurn;
    public MarketWidget() {
        this.gridPaneContent = new ImageView[3][5];
        this.ownTurn = new SimpleBooleanProperty(false);
        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        ownTurn.bind(GUI.instance().ownTurnProperty());

        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 6; j++) {
                if(j == 0 && i != 4 || i == 0 && j != 5) {
                    if(i != 3) {
                        BorderPane slideCell = new BorderPane();
                        slideCell.setPrefHeight(100);
                        slideCell.setPrefWidth(100);
                        slideCell.getStyleClass().add("slide");
                        marketDisplay.add(slideCell, j, i, 1, 1);
                    }
                } else if(j == 5 || i == 4) {
                    BorderPane marginCell = new BorderPane();
                    marginCell.setPrefHeight(100);
                    marginCell.setPrefWidth(100);
                    marginCell.getStyleClass().add("margin");
                    marketDisplay.add(marginCell, j, i, 1, 1);
                    if(i == 4 && j != 0 && j != 5) {
                        marginCell.setCenter(new Label("^"));
                        //TODO Implement actual drag and drop
                        marginCell.setOnDragOver(event -> {
                            if (event.getGestureSource() instanceof ImageView &&
                                    event.getDragboard().hasString() && ownTurn.get()) {
                                event.acceptTransferModes(TransferMode.ANY);
                            }

                            event.consume();
                        });
                        marginCell.setOnDragDropped(event -> {
                            Dragboard db = event.getDragboard();
                            event.setDropCompleted(true);

                            event.consume();
                        });
                    } else if(j == 5 && i != 0 && i != 4) {
                        marginCell.setCenter(new Label("<"));
                        marginCell.setOnDragOver(event -> {
                            if (event.getGestureSource() instanceof ImageView &&
                                    event.getDragboard().hasString() && ownTurn.get()) {
                                event.acceptTransferModes(TransferMode.ANY);
                            }
                            event.consume();
                        });
                        marginCell.setOnDragDropped(event -> {
                            Dragboard db = event.getDragboard();
                            event.setDropCompleted(true);

                            event.consume();
                        });
                    }
                } else {
                    ImageView emptyImg = new ImageView();
                    BorderPane imgWrapper = new BorderPane(emptyImg);
                    imgWrapper.getStyleClass().add("table");
                    marketDisplay.add(imgWrapper, j, i, 1, 1);
                    gridPaneContent[i - 1][j] = emptyImg;
                }
            }
        }
        ImageView emptyImg = new ImageView();
        emptyImg.setOnDragDetected(mouseEvent -> {
            Dragboard db = emptyImg.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();
            content.putString("RES");
            content.putImage(emptyImg.getImage());
            db.setContent(content);

            System.out.println(content.getString());

            mouseEvent.consume();
        });
        BorderPane imgWrapper = new BorderPane(emptyImg);
        imgWrapper.getStyleClass().add("slide");
        marketDisplay.add(imgWrapper, 0, 3, 1, 1);
        gridPaneContent[2][0] = emptyImg;

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
        slideResource.addListener((change, oldValue, newValue) -> {
            updateSlideResource(newValue);
        });
    }

    private void updateRow(int index, List<Resource> row) {
        Platform.runLater(() -> {
            for(int i = 1; i < 5; i++) {
                gridPaneContent[index][i].setImage(GUIUtils.getResourceImage(row.get(i - 1), 80, 80));
            }
        });
    }

    private void updateSlideResource(Resource resource) {
        Platform.runLater(() -> {
            gridPaneContent[2][0].setImage(GUIUtils.getResourceImage(resource, 80, 80));
        });
    }
}
