package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.widget.MusicButtonWidget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Home {
    @FXML
    public VBox leftVBox;
    @FXML
    private TextField serverIp;
    @FXML
    private Button connect;
    @FXML
    private Label errorDisplay;

    @FXML
    private void initialize() {
        MusicButtonWidget musicButtonWidget = new MusicButtonWidget(true);
        leftVBox.getChildren().add(musicButtonWidget);
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
