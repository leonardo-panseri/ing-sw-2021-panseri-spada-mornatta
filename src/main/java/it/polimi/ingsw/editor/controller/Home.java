package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.fxml.FXML;

public class Home {
    @FXML
    public void goToEditLeaderCards() {
        GameConfigEditor.goToEditLeaderCards();
    }

    @FXML
    public void goToEditDevelopmentCards() {
        GameConfigEditor.goToEditDevelopmentCards();
    }

    @FXML
    public void goToEditBaseProduction() {
        GameConfigEditor.goToEditBaseProduction();
    }

    @FXML
    public void goToEditFaithTrack() {
        GameConfigEditor.goToEditFaithTrack();
    }

    @FXML
    public void initialize() {
    }
}
