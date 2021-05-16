package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class EditDevelopmentCards {

    @FXML
    public VBox developmentCardsContainer;

    @FXML
    private void initialize() {

    }

    @FXML
    public void goToHome() {
        GameConfigEditor.goToHome();
    }
}
