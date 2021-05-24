package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.messages.InitialSelectionPlayerActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.*;

public class LeaderSelectionWidget extends HBox {
    @FXML
    public HBox leadersDisplay;
    @FXML
    public Button confirmButton;

    private final Set<LeaderCard> leaderCards;
    private final Map<LeaderCard, Boolean> cardsChoice;
    private boolean done;

    private List<UUID> chosenCards;
    public LeaderSelectionWidget(Set<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
        this.cardsChoice = new HashMap<>();
        leaderCards.forEach(card -> cardsChoice.put(card, false));
        this.done = false;
        this.chosenCards = new ArrayList<>();

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        for(LeaderCard card : leaderCards) {
            LeaderCardWidget leaderCardWidget = new LeaderCardWidget(card);
            leadersDisplay.getChildren().add(leaderCardWidget);
            leaderCardWidget.setOnMouseClicked(mouseEvent -> {
                if(!leaderCardWidget.isCardFlipped() && done) {
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

    private void checkIfDone() {
        int found = 0;
        for(boolean active : cardsChoice.values()) {
            if(active) found++;
        }
        done = found > 1;
        confirmButton.setDisable(!done);
    }

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
        if(initialResourcesToChoose > 0) {
            goToChooseResources(initialResourcesToChoose);

            gui.setGameState(GameState.CHOOSING_RESOURCES);
        } else {
            gui.getActionSender().selectLeaders(chosenCards, new HashMap<>());
            if(!gui.getClient().isNoServer())
                gui.setGameState(GameState.WAIT_SELECT_LEADERS);
        }
    }

    private void goToChooseResources(int initialResourcesToChoose) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        int initialFaith = GUI.instance().getModel().getLocalPlayer().getFaithPoints();
        if(initialFaith != 0) {
            Label faithLabel = new Label("You start with " + initialFaith + " faith point!");
            box.getChildren().add(faithLabel);
        }

        Label chooseResLabel = new Label("You must choose " + initialResourcesToChoose + " starting resources!");
        box.getChildren().add(chooseResLabel);

        HBox dragArea = new HBox();
        dragArea.setAlignment(Pos.CENTER);
        Pane depositDisplay = new Pane();

    }
}
