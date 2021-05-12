package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.model.card.LeaderCard;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class EditLeaderCards {
    @FXML
    public VBox leaderCardsContainer;

    @FXML
    public void goToHome() {
        GameConfigEditor.goToHome();
    }

    @FXML
    public void initialize() {

    }
}
