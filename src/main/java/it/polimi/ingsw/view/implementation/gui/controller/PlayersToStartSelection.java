package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PlayersToStartSelection {
    @FXML
    private TextField playersToStart;
    @FXML
    private Button choose;

    @FXML
    private void initialize() {
        choose.setOnAction(actionEvent -> GUI.instance().getActionSender()
                .setPlayersToStart(Integer.parseInt(playersToStart.getText())));
    }
}