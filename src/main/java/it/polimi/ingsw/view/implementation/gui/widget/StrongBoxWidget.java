package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StrongBoxWidget extends FlowPane {
    private HBox servantRow;
    private HBox coinRow;
    private HBox shieldRow;
    private HBox stoneRow;

    private final MockPlayer player;

    public StrongBoxWidget(MockPlayer player) {
        this.player = player;
        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        for (Resource resource : player.getDeposit().strongBoxProperty().keySet()) {
            createStrongBoxRow(resource, player.getDeposit().strongBoxProperty().get(resource));
        }

        player.getDeposit().strongBoxProperty().addListener((MapChangeListener<? super Resource, ? super Integer>) change -> {
            int numberOf;
            Resource resType = change.getKey();

            if (change.wasAdded() && !change.wasRemoved()) {
                numberOf = change.getValueAdded();
                createStrongBoxRow(resType, numberOf);
            } else if (change.wasRemoved() && change.wasAdded()) {
                numberOf = change.getValueAdded();
                updateStrongBox(resType, numberOf);
            } else {
                removeStrongBoxRow(resType);
            }
        });
    }

    public void createStrongBoxRow(Resource resType, int numberOf) {
        Platform.runLater(() -> {
            Image image = GUIUtils.getResourceImage(resType, 50, 50);
            ImageView currentImg = new ImageView(image);
            Label label = new Label("" + numberOf);
            VBox centeredLabel = new VBox(label);
            centeredLabel.setAlignment(Pos.CENTER);

            switch (resType) {
                case COIN -> {
                    coinRow = new HBox();
                    coinRow.getChildren().add(currentImg);
                    coinRow.getChildren().add(centeredLabel);
                    this.getChildren().add(coinRow);
                }
                case STONE -> {
                    stoneRow = new HBox();
                    stoneRow.getChildren().add(currentImg);
                    stoneRow.getChildren().add(centeredLabel);
                    this.getChildren().add(stoneRow);
                }
                case SHIELD -> {
                    shieldRow = new HBox();
                    shieldRow.getChildren().add(currentImg);
                    shieldRow.getChildren().add(centeredLabel);
                    this.getChildren().add(shieldRow);
                }
                case SERVANT -> {
                    servantRow = new HBox();
                    servantRow.getChildren().add(currentImg);
                    servantRow.getChildren().add(centeredLabel);
                    this.getChildren().add(servantRow);
                }
            }
        });
    }

    public void updateStrongBox(Resource resType, int numberOf) {
        Platform.runLater(() -> {
            switch (resType) {
                case COIN -> ((Label) ((VBox) coinRow.getChildren().get(1)).getChildren().get(0)).setText("" + numberOf);
                case SERVANT -> ((Label) ((VBox) servantRow.getChildren().get(1)).getChildren().get(0)).setText("" + numberOf);
                case STONE -> ((Label) ((VBox) stoneRow.getChildren().get(1)).getChildren().get(0)).setText("" + numberOf);
                case SHIELD -> ((Label) ((VBox) shieldRow.getChildren().get(1)).getChildren().get(0)).setText("" + numberOf);
            }
        });
    }

    public void removeStrongBoxRow(Resource resource) {
        switch (resource) {
            case COIN -> this.getChildren().remove(coinRow);
            case SERVANT -> this.getChildren().remove(servantRow);
            case STONE -> this.getChildren().remove(stoneRow);
            case SHIELD -> this.getChildren().remove(shieldRow);
        }
    }
}

