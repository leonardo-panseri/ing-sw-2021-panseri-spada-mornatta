package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Page for the selection of the user name.
 */

public class NameSelection {
    @FXML
    private TextField username;
    @FXML
    private Button choose;

    @FXML
    private void initialize() {
        choose.setOnAction(actionEvent -> {
            if (!username.getText().isBlank())
                GUI.instance().setPlayerName(username.getText());
        });
    }
}
