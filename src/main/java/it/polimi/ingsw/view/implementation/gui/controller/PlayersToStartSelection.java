package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PlayersToStartSelection {
    @FXML
    private TextField playersToStart;
    @FXML
    private Button choose;
    @FXML
    private Label errorDisplay;

    @FXML
    private void initialize() {
        choose.setOnAction(actionEvent -> {
            int num;
            try {
                num = Integer.parseInt(playersToStart.getText());
                if (num < 1 || num > 4)
                    throw new RuntimeException();
            } catch (Exception e) {
                errorDisplay.setText("Must be a number between 1 and 4");
                return;
            }
            GUI.instance().getActionSender().setPlayersToStart(num);
        });
    }
}
