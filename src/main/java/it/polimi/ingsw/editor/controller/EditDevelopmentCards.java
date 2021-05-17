package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class EditDevelopmentCards {

    @FXML
    public VBox developmentCardsContainer;
    @FXML
    public Accordion levelsAccordion;

    @FXML
    private void initialize() {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> devCards = GameConfigEditor.getGameConfig().getDevelopmentCards();

        for(int i = 0; i < devCards.size(); i++) {
            Accordion colorsAccordion = new Accordion();

            for(CardColor color : CardColor.values()) {
                List<DevelopmentCardWidget> cards = new ArrayList<>();

                devCards.get(i).get(color).forEach(card -> cards.add(new DevelopmentCardWidget(card)));

                ListView<DevelopmentCardWidget> devCardView = new ListView<>(FXCollections.observableList(cards));
                devCardView.getStyleClass().add("dev-cards-list");
                devCardView.setOrientation(Orientation.HORIZONTAL);
                TitledPane colorPane = new TitledPane(color.toString(), devCardView);
                colorsAccordion.getPanes().add(colorPane);
                colorsAccordion.getStyleClass().add("dev-cards-colors");
            }

            TitledPane levelPane = new TitledPane("Level " + (i+1), colorsAccordion);
            levelsAccordion.getPanes().add(levelPane);
        }


    }

    @FXML
    public void goToHome() {
        GameConfigEditor.goToHome();
    }
}
