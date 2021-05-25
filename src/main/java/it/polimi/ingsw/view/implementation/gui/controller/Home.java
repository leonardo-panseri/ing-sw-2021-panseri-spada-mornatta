package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Home {
    @FXML
    private TextField serverIp;
    @FXML
    private Button connect;
    @FXML
    private Label errorDisplay;

    @FXML
    private void initialize() {
        serverIp.setText("localhost:12345");
        serverIp.setOnMouseClicked(mouseEvent -> serverIp.selectAll());

        connect.setOnAction(actionEvent -> {
            if(!GUI.instance().getClient().connect()) {
                errorDisplay.setText("Can't connect to the server!");
                errorDisplay.setVisible(true);
            }
        });
    }
}
