package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.beans.MockPlayer;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class DepositWidget extends AnchorPane {
    @FXML
    public HBox topRow;
    @FXML
    public HBox middleRow;
    @FXML
    public HBox bottomRow;

    private final MockPlayer player;
    public DepositWidget(MockPlayer player) {
        this.player = player;

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


    }

    private void updateRow(int index, List<Resource> resources) {
        Platform.runLater(() -> {
            switch (index) {
                case 0 -> {
                    topRow.getChildren().clear();
                    resources.forEach(resource -> topRow.getChildren().add(getResourceImage(resource)));
                }
                case 1 -> {
                    middleRow.getChildren().clear();
                    resources.forEach(resource -> middleRow.getChildren().add(getResourceImage(resource)));
                }
                case 2 -> {
                    bottomRow.getChildren().clear();
                    resources.forEach(resource -> bottomRow.getChildren().add(getResourceImage(resource)));
                }
            }
        });
    }

    private ImageView getResourceImage(Resource resource) {
        InputStream imgIs = getClass().getResourceAsStream("/images/resources/" + resource.toString().toLowerCase() + ".png");
        if(imgIs == null) {
            System.err.println("Image not found for " + resource);
            return new ImageView();
        }
        return new ImageView(new Image(imgIs, 39.0, 33.0, true, true));
    }
}
