package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class Home {
    @FXML
    public ToggleGroup connection;
    @FXML
    public RadioButton onlineGame;
    @FXML
    public RadioButton localGame;
    @FXML
    public TextField serverIp;
    @FXML
    public Button connect;

    @FXML
    private void initialize() {
        connection.selectedToggleProperty().addListener((ov, old_toggle,new_toggle) -> {
            if(localGame.isSelected()) {
                serverIp.setText("localhost:12345");
                serverIp.setEditable(false);
                serverIp.setOpacity(0.4);
            } else {
                serverIp.setText("");
                serverIp.setEditable(true);
                serverIp.setOpacity(1);
            }
        });

        connect.setOnAction(actionEvent -> GUI.instance().getClient().connect());
    }
}
