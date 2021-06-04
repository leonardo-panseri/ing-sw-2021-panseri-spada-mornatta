package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.MediaPlayer;

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

    public void toggleMusic() {
        MediaPlayer mediaPlayer = GUI.instance().getMediaPlayer();
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) mediaPlayer.pause();
        else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) mediaPlayer.play();
    }
}
