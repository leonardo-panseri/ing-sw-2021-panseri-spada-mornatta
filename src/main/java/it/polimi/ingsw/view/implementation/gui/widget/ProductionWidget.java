package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class ProductionWidget extends FlowPane {
    @FXML
    private VBox resourcesDisplay;
    @FXML
    private HBox productionDisplay;
    @FXML
    private Button exitButton;
    @FXML
    private Button confirmButton;

    private final PlayerBoardWidget playerBoard;
    private final List<Resource> input;
    private final List<Resource> output;

    public ProductionWidget(PlayerBoardWidget playerBoard, List<Resource> input, List<Resource> output) {
        this.playerBoard = playerBoard;
        this.input = input;
        this.output = output;

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        buildResourcesDisplay();
        buildProductionDisplay();
    }

    private void buildResourcesDisplay() {
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            ImageView img = new ImageView(GUIUtils.getResourceImage(resource, 50, 50));
            Label quantity = new Label("" + playerBoard.getPlayer().getDeposit().countResource(resource));
            VBox textBox = new VBox(quantity);
            textBox.setAlignment(Pos.CENTER);
            HBox box = new HBox(img, textBox);
            box.setSpacing(10);
            resourcesDisplay.getChildren().add(box);

            img.setOnDragDetected(mouseEvent -> {
                Dragboard db = img.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString(resource.toString());
                content.putImage(img.getImage());
                db.setContent(content);

                mouseEvent.consume();
            });
        });
    }

    private void buildProductionDisplay() {
        productionDisplay.getChildren().add(buildResourcesPane(input));

        ImageView separator = new ImageView();
        productionDisplay.getChildren().add(separator);

        productionDisplay.getChildren().add(buildResourcesPane(output));
    }

    private FlowPane buildResourcesPane(List<Resource> list) {
        FlowPane pane = new FlowPane(20, 20);
        pane.setPrefWidth(300);
        pane.setPrefHeight(300);
        for(Resource res : list) {
            ImageView img = new ImageView(GUIUtils.getResourceImage(res, 50, 50));
            pane.getChildren().add(img);
        }
        return pane;
    }

    @FXML
    private void closeProductionModal() {
        playerBoard.closeProductionModal();
    }
}
