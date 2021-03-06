package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.function.Consumer;

/**
 * Widget that is used at the beginning of the match to let the player decide which leader cards to keep and,
 * optionally, which resources to take.
 */

public class LeaderSelectionWidget extends HBox {
    @FXML
    private HBox leadersDisplay;
    @FXML
    private Button confirmButton;

    private final Set<LeaderCard> leaderCards;
    private final Map<LeaderCard, Boolean> cardsChoice;
    private boolean done;

    private final List<UUID> chosenCards;
    private final Map<Integer, List<Resource>> chosenResources;
    private int chosenResourcesCount;

    /**
     * Creates a new LeaderSelectionWidget with the given 4 leader cards.
     * @param leaderCards the 4 starting leader cards of a player
     */
    public LeaderSelectionWidget(Set<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
        this.cardsChoice = new HashMap<>();
        this.chosenResources = new HashMap<>();
        leaderCards.forEach(card -> cardsChoice.put(card, true));
        this.done = false;
        this.chosenCards = new ArrayList<>();
        this.chosenResourcesCount = 0;

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        for (LeaderCard card : leaderCards) {
            LeaderCardWidget leaderCardWidget = new LeaderCardWidget(card);
            leadersDisplay.getChildren().add(leaderCardWidget);
            leaderCardWidget.setOnMouseClicked(mouseEvent -> {
                if (!leaderCardWidget.isCardFlipped() && done) {
                    return;
                }
                leaderCardWidget.flipCard();
                cardsChoice.put(leaderCardWidget.getLeaderCard(), !cardsChoice.get(leaderCardWidget.getLeaderCard()));
                checkIfDone();
            });
        }

        confirmButton.setOnAction(actionEvent -> {
            chooseCards();
            confirmButton.setDisable(true);
        });
    }

    /**
     * Checks if the player has already chosen 2 cards to discard.
     */
    private void checkIfDone() {
        int found = 0;
        for (boolean active : cardsChoice.values()) {
            if (!active) found++;
        }
        done = found > 1;
        confirmButton.setDisable(!done);
    }

    /**
     * Keeps the selected leader card and, if the player has to chose resources too, goes to resource selection screen.
     */
    private void chooseCards() {
        int added = 0;
        for (Map.Entry<LeaderCard, Boolean> entry : cardsChoice.entrySet()) {
            if (added < 2 && entry.getValue()) {
                chosenCards.add(entry.getKey().getUuid());
                added++;
            }
        }

        GUI gui = GUI.instance();
        int initialResourcesToChoose = gui.getModel().getLocalPlayer().getInitialResourcesToChoose();
        if (initialResourcesToChoose > 0) {
            goToChooseResources(initialResourcesToChoose);

            gui.setGameState(GameState.CHOOSING_RESOURCES);
        } else {
            confirmSelection();
        }
    }

    /**
     * Goes to the resource selection screen.
     * @param initialResourcesToChoose how many resources the player has to choose
     */
    private void goToChooseResources(int initialResourcesToChoose) {
        BorderPane box = new BorderPane();
        box.getStyleClass().add("selection-box");
        box.setPrefWidth(700);
        box.setPrefHeight(400);
        box.setMaxWidth(700);
        box.setMaxHeight(400);
        int initialFaith = GUI.instance().getModel().getLocalPlayer().getFaithPoints();
        String titleText = "";
        if (initialFaith != 0) {
            titleText += "You start with " + initialFaith + " faith point!\n";
        }

        Label chooseResLabel = new Label(titleText + "You must choose " + initialResourcesToChoose + " starting resources!");
        chooseResLabel.getStyleClass().add("leader-select-title");
        HBox title = new HBox(chooseResLabel);
        title.setAlignment(Pos.CENTER);
        box.setTop(title);

        Button chooseConfirmButton = new Button("Confirm");
        chooseConfirmButton.setDisable(true);
        chooseConfirmButton.setOnAction(actionEvent -> confirmSelection());

        DepositWidget depositWidget = new DepositWidget(GUI.instance().getModel().getLocalPlayer());
        depositWidget.setDropAllowed(true);
        depositWidget.setOnDragDroppedHandler(buildOnDragDroppedListener(initialResourcesToChoose, chooseConfirmButton));

        VBox resourcePicker = buildResourcePicker(initialResourcesToChoose);
        resourcePicker.setMaxHeight(depositWidget.getPrefHeight());
        HBox dragArea = new HBox(depositWidget, resourcePicker);
        dragArea.setAlignment(Pos.CENTER);
        dragArea.setSpacing(40);
        dragArea.getStyleClass().add("choose-resources");
        box.setCenter(dragArea);

        HBox buttonBox = new HBox(chooseConfirmButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        box.setBottom(buttonBox);

        getChildren().clear();
        getChildren().add(box);
    }

    /**
     * Creates a consumer function for the drag and drop event on one of the free slots of resources.
     * This function adds the chosen resources to the player's deposit if the event has succeeded.
     * @param initialResourcesToChoose how many resources the player has to chose
     * @param chooseConfirmButton the button that lets the player confirm his choice
     * @return the consumer function
     */
    private Consumer<DragEvent> buildOnDragDroppedListener(int initialResourcesToChoose, Button chooseConfirmButton) {
        return event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            Resource res = null;
            int rowIndex = -1;
            if (db.hasString()) {
                success = true;
                try {
                    res = Resource.valueOf(db.getString());

                    rowIndex = DepositWidget.getRowId(event.getGestureTarget());
                } catch (Exception ignored) {
                    success = false;
                }
            }
            if (success) {
                if (chosenResourcesCount < initialResourcesToChoose) {
                    success = GUI.instance().getModel().getLocalPlayer().getDeposit().addToRow(rowIndex, res);

                    if (success) {
                        chosenResourcesCount++;

                        if (chosenResourcesCount >= initialResourcesToChoose)
                            Platform.runLater(() -> chooseConfirmButton.setDisable(false));

                        if (chosenResources.containsKey(rowIndex + 1))
                            chosenResources.get(rowIndex + 1).add(res);
                        else
                            chosenResources.put(rowIndex + 1, new ArrayList<>(Collections.singletonList(res)));
                    }
                } else
                    success = false;
            }
            event.setDropCompleted(success);

            event.consume();
        };
    }

    /**
     * Sends a player action event to the server via ActionSender with the chosen card and chosen resources.
     */
    private void confirmSelection() {
        GUI.instance().getActionSender().selectLeaders(chosenCards, chosenResources);
        if (!GUI.instance().getClient().isNoServer())
            GUI.instance().setGameState(GameState.WAIT_SELECT_LEADERS);
    }

    /**
     * Builds an image view for each resource, so that the player can drag them and drop them in the free slots
     * when choosing initial resources.
     * @param initialResourcesToChoose the number of resources the player has to choose
     * @return a VBox containing a draggable image view for each resource
     */
    private VBox buildResourcePicker(int initialResourcesToChoose) {
        VBox box = new VBox();
        for (Resource resource : Resource.values()) {
            if (resource == Resource.FAITH)
                continue;
            ImageView img = new ImageView(GUIUtils.getResourceImage(resource, 50, 50));

            img.setOnDragDetected(mouseEvent -> {
                if (chosenResourcesCount < initialResourcesToChoose) {
                    Dragboard db = img.startDragAndDrop(TransferMode.ANY);

                    db.setContent(GUIUtils.getClipboardForResource(resource, img));

                    mouseEvent.consume();
                }
            });

            box.getChildren().add(img);
        }
        box.setSpacing(20);
        return box;
    }
}
