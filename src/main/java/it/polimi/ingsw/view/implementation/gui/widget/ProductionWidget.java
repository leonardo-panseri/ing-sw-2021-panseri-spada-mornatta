package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import it.polimi.ingsw.view.messages.production.Production;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.*;

/**
 * Widget to create and queue a production.
 */
public class ProductionWidget extends FlowPane {
    @FXML
    private VBox resourcesDisplay;
    @FXML
    private HBox productionDisplay;
    @FXML
    private Button confirmButton;

    private final PlayerBoardWidget playerBoard;
    private final List<Resource> input;
    private final List<Resource> output;
    private final Class<? extends Production> productionType;
    private Card card;

    private final List<Resource> desiredInput;
    private final List<Resource> desiredOutput;

    private final Map<Resource, StringProperty> resourcesCount;

    /**
     * Constructs a new ProductionWidget.
     *
     * @param playerBoard the player board that this production widget was opened from
     * @param input a list of resources that are the input of the production, if one element is null that means that the
     *             slot can accept any resource
     * @param output a list of resources that are the output of the production, if one element is null that means that the
     *      *             slot can accept any resource
     * @param productionType the type of this production
     */
    public ProductionWidget(PlayerBoardWidget playerBoard, List<Resource> input, List<Resource> output,
                            Class<? extends Production> productionType) {
        this.playerBoard = playerBoard;
        this.input = input;
        this.output = output;
        this.productionType = productionType;

        this.desiredInput = new ArrayList<>();
        this.desiredOutput = new ArrayList<>();

        this.resourcesCount = new HashMap<>();

        FXMLUtils.loadWidgetFXML(this);
    }

    /**
     * Constructs a new ProductionWidget.
     *
     * @param playerBoard the player board that this production widget was opened from
     * @param input a list of resources that are the input of the production, if one element is null that means that the
     *             slot can accept any resource
     * @param output a list of resources that are the output of the production, if one element is null that means that the
     *      *             slot can accept any resource
     * @param productionType the type of this production
     * @param card the card that this production is from
     */
    public ProductionWidget(PlayerBoardWidget playerBoard, List<Resource> input, List<Resource> output,
                            Class<? extends Production> productionType, Card card) {
        this(playerBoard, input, output, productionType);
        this.card = card;
    }

    @FXML
    private void initialize() {
        buildResourcesDisplay();
        buildProductionDisplay();
    }

    /**
     * Builds a display that shows how man resources the player has available for the production.
     */
    private void buildResourcesDisplay() {
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            ImageView img = new ImageView(GUIUtils.getResourceImage(resource, 50, 50));
            Label quantity = new Label("" + playerBoard.getPlayer().getDeposit().countResource(resource));
            resourcesCount.put(resource, quantity.textProperty());
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

    /**
     * Builds the display for the production.
     */
    private void buildProductionDisplay() {
        productionDisplay.getChildren().add(buildResourcesPane(input, true));

        InputStream is = ProductionWidget.class.getResourceAsStream("/images/parentheses.png");
        if (is != null) {
            ImageView separator = new ImageView(new Image(is, 50, 300, false, true));
            productionDisplay.getChildren().add(separator);
        }

        productionDisplay.getChildren().add(buildResourcesPane(output, false));
    }

    /**
     * Builds a pane containing the images of the resources from the list,
     *
     * @param list a list of resources
     * @param opaque true if the images should be opaque
     * @return a pane containing images for the resources
     */
    private FlowPane buildResourcesPane(List<Resource> list, boolean opaque) {
        FlowPane pane = new FlowPane(20, 20);
        pane.setAlignment(Pos.CENTER);
        pane.setPrefWidth(300);
        pane.setPrefHeight(300);
        for (Resource res : list) {
            ImageView img = new ImageView(GUIUtils.getResourceImage(res, 50, 50));

            if (opaque) {
                img.setOpacity(0.4);
                img.setOnDragOver(dragOverHandler(img));
                img.setOnDragDropped(dragDroppedHandler(img, res, true));
            } else if (res == null) {
                img.setOpacity(0.4);
                img.setOnDragOver(dragOverHandler(img));
                img.setOnDragDropped(dragDroppedHandler(img, null, false));
            } else desiredOutput.add(res);

            pane.getChildren().add(img);
        }
        return pane;
    }

    /**
     * Gets the drag over handler for the given ImageView.
     *
     * @param img the image view
     * @return the event handler
     */
    private EventHandler<DragEvent> dragOverHandler(ImageView img) {
        return dragEvent -> {
            if (dragEvent.getGestureSource() instanceof ImageView && dragEvent.getDragboard().hasString() &&
                    img.getOpacity() < 1) {
                dragEvent.acceptTransferModes(TransferMode.ANY);
            }

            dragEvent.consume();
        };
    }

    /**
     * Gets the drag dropped handler for the given ImageView.
     *
     * @param img the image view
     * @param targetResource the resource that the image view is displaying
     * @param isInput true if the image view is part of the input section, false otherwise
     * @return the event handler
     */
    private EventHandler<DragEvent> dragDroppedHandler(ImageView img, Resource targetResource, boolean isInput) {
        return dragEvent -> {
            Dragboard db = dragEvent.getDragboard();

            boolean success = true;
            Resource resource = null;
            try {
                resource = Resource.valueOf(db.getString());
            } catch (IllegalArgumentException e) {
                success = false;
            }

            if (success) {
                if (isInput) {
                    int quantity = Integer.parseInt(resourcesCount.get(resource).get());
                    if (quantity > 0 && (targetResource == null || resource == targetResource)) {
                        quantity--;
                        resourcesCount.get(resource).setValue("" + quantity);
                    } else success = false;
                }

                if (success) {
                    if (targetResource == null) {
                        Resource finalResource = resource;
                        Platform.runLater(() -> img.setImage(GUIUtils.getResourceImage(finalResource, 50, 50)));
                    } else if (resource != targetResource) {
                        success = false;
                    }
                }

                if (success) {
                    Platform.runLater(() -> img.setOpacity(1));

                    if (isInput)
                        desiredInput.add(resource);
                    else
                        desiredOutput.add(resource);

                    checkIfDone();
                }
            }

            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        };
    }

    /**
     * Check if the production is completed and can be queued.
     */
    private void checkIfDone() {
        Platform.runLater(() -> {
            if (input.size() == desiredInput.size() && output.size() == desiredOutput.size())
                confirmButton.setDisable(false);
        });
    }

    /**
     * Queues the production and closes this widget.
     */
    @FXML
    private void queueProduction() {
        switch (productionType.getSimpleName()) {
            case "BaseProduction" -> GUI.instance().getActionSender().useBaseProduction(desiredInput, desiredOutput);
            case "DevelopmentProduction" -> GUI.instance().getActionSender().useDevelopmentProduction((DevelopmentCard) card);
            case "LeaderProduction" -> GUI.instance().getActionSender().useLeaderProduction((LeaderCard) card, desiredOutput.get(0));
        }
        closeProductionModal();
    }

    /**
     * Closes this widget.
     */
    @FXML
    private void closeProductionModal() {
        playerBoard.closeProductionModal();
    }
}
