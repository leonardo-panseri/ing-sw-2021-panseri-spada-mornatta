package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.LeaderCardRequirement;
import it.polimi.ingsw.model.card.SpecialAbility;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

public class LeaderCardWidget extends StackPane {
    @FXML
    private AnchorPane cardPane;
    @FXML
    private FlowPane lcRequirements;
    @FXML
    private Label lcVictoryPoints;
    @FXML
    private Pane cardBack;

    private final LeaderCard leaderCard;
    private boolean cardFlipped;

    public LeaderCardWidget(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;
        this.cardFlipped = false;

        FXMLUtils.loadWidgetFXML(this);
    }

    public LeaderCard getLeaderCard() {
        return leaderCard;
    }

    @FXML
    private void initialize() {
        LeaderCardRequirement requirements = leaderCard.getCardRequirements();
        requirements.getResourceRequirements().forEach((resource, quantity) ->
                lcRequirements.getChildren().add(GUIUtils.buildResourceDisplay("resources/" + resource.toString().toLowerCase(), quantity))); //stone.png
        requirements.getCardColorRequirements().forEach((color, quantity) ->
                lcRequirements.getChildren().add(GUIUtils.buildResourceDisplay("leaders/flags/" + color.toString().toLowerCase(), quantity))); //blue.png
        requirements.getCardLevelRequirements().forEach((color, level) ->
                lcRequirements.getChildren().add(GUIUtils.buildResourceDisplay("leaders/flags/" + color.toString().toLowerCase() + level, 1))); //red1.png

        lcVictoryPoints.textProperty().set(Integer.toString(leaderCard.getVictoryPoints()));

        Image bgImage = new Image(Objects.requireNonNull(LeaderCardWidget.class.getResourceAsStream(
                "/images/leaders/" + leaderCard.getSpecialAbility().getType().toString().toLowerCase() + "Leader.png")),
                195.0, 294.0, true, true); //production.png
        Background background = new Background(new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        cardPane.setBackground(background);
        List<ImageView> specialAbility = buildSpecialAbility(leaderCard.getSpecialAbility());
        cardPane.getChildren().addAll(specialAbility);
    }

    private List<ImageView> buildSpecialAbility(SpecialAbility specialAbility) {
        String resource = specialAbility.getTargetResource().toString().toLowerCase();
        InputStream image = LeaderCardWidget.class.getResourceAsStream("/images/resources/" + resource + ".png");

        if (image == null) {
            System.err.println("Can't find image for " + resource);
            return Collections.emptyList();
        }

        List<ImageView> results = new ArrayList<>();
        switch (specialAbility.getType()) {
            case PRODUCTION -> {
                ImageView input = new ImageView(new Image(image, 30.0, 41.0, true, true));
                input.setLayoutX(35);
                input.setLayoutY(237);
                results.add(input);
            }
            case DISCOUNT -> {
                ImageView discount = new ImageView(new Image(image, 22, 22, true, true));
                discount.setLayoutX(144);
                discount.setLayoutY(227);
                results.add(discount);
            }
            case DEPOT -> {
                Image img = new Image(image, 39, 41, true, true);
                ImageView depot1 = new ImageView(img);
                depot1.setLayoutX(42);
                depot1.setLayoutY(233);
                depot1.setOpacity(0.4);
                results.add(depot1);
                ImageView depot2 = new ImageView(img);
                depot2.setLayoutX(116);
                depot2.setLayoutY(233);
                depot2.setOpacity(0.4);
                results.add(depot2);

                if (GUI.instance() != null) {
                    if (GUI.instance().getScene().getRoot() instanceof PlayerBoardWidget) {
                        PlayerBoardWidget playerBoard = (PlayerBoardWidget) GUI.instance().getScene().getRoot();
                        if (playerBoard.getPlayer().isLocalPlayer() && GUI.instance().getGameState() == GameState.PLAYING) {
                            registerDepositHandler(Arrays.asList(depot1, depot2), playerBoard);
                        }
                    }
                }
            }
            case EXCHANGE -> {
                ImageView exchange = new ImageView(new Image(image, 47, 55, true, true));
                exchange.setLayoutX(116);
                exchange.setLayoutY(227);
                results.add(exchange);
            }
        }
        return results;
    }

    private void registerDepositHandler(List<ImageView> images, PlayerBoardWidget playerBoard) {
        int depositIndex = playerBoard.getPlayer().getDeposit().getLeaderDepositIndexForCard(leaderCard);
        updateDeposit(images, playerBoard.getPlayer().getDeposit().getLeadersDeposit(depositIndex));
        playerBoard.getPlayer().getDeposit().leaderDepositProperty().addListener(
                (MapChangeListener<? super Integer, ? super List<Resource>>) change -> {
                    if (change.getKey() == depositIndex && change.wasAdded()) {
                        updateDeposit(images, change.getValueAdded());
                    }
                });

        for (ImageView img : images) {
            img.setOnDragDetected(mouseEvent -> {
                if (img.getOpacity() == 1 && playerBoard.getPlayer().isLocalPlayer() && GUI.instance().isOwnTurn()) {
                    Dragboard db = img.startDragAndDrop(TransferMode.ANY);

                    ClipboardContent content = new ClipboardContent();
                    content.putString("leaderDeposit" + depositIndex);
                    content.putImage(img.getImage());
                    db.setContent(content);

                    playerBoard.getDepositWidget().setDropAllowed(true);
                    playerBoard.getDepositWidget().setOnDragDroppedHandler(depositResourceMoveHandler(depositIndex));

                    mouseEvent.consume();
                }
            });
            img.setOnDragDone(dragEvent -> playerBoard.getDepositWidget().setDropAllowed(false));

            img.setOnDragOver(dragEvent -> {
                if (dragEvent.getGestureSource() instanceof ImageView && GUI.instance().isOwnTurn()) {
                    dragEvent.acceptTransferModes(TransferMode.ANY);
                }

                dragEvent.consume();
            });
            img.setOnDragDropped(dragEvent -> {
                Dragboard db = dragEvent.getDragboard();
                boolean success = true;
                boolean fromMarketResults = false;
                int sourceIndex = -1;
                try {
                    String sourceType = db.getString();
                    if (sourceType != null && sourceType.startsWith("marketResult")) {
                        sourceIndex = Integer.parseInt(db.getString().replace("marketResult", ""));
                        fromMarketResults = true;
                    }
                    else sourceIndex = DepositWidget.getRowId(((Node)dragEvent.getGestureSource()).getParent());
                } catch (IllegalArgumentException e) {
                    success = false;
                }

                if (success) {
                    if (fromMarketResults) GUI.instance().getActionSender().storeMarketResult(sourceIndex, depositIndex + 3);
                    else GUI.instance().getActionSender().move(sourceIndex + 1, depositIndex + 3);
                }

                dragEvent.setDropCompleted(success);
                dragEvent.consume();
            });
        }
    }

    private Consumer<DragEvent> depositResourceMoveHandler(int depositIndex) {
        return dragEvent -> {
            boolean success = true;
            int rowIndex = -1;
            try {
                rowIndex = DepositWidget.getRowId(dragEvent.getGestureTarget());
            } catch (IllegalArgumentException e) {
                success = false;
            }

            if (success) {
                GUI.instance().getActionSender().move(depositIndex + 3, rowIndex + 1);
            }

            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        };
    }

    private void updateDeposit(List<ImageView> images, List<Resource> resources) {
        for (int i = 1; i <= 2; i++) {
            if (i <= resources.size())
                images.get(i - 1).setOpacity(1);
            else
                images.get(i - 1).setOpacity(0.4);
        }
    }

    public void flipCard() {
        cardFlipped = !cardFlipped;
        cardBack.setVisible(cardFlipped);
    }

    public boolean isCardFlipped() {
        return cardFlipped;
    }
}
