package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class WaitingPlayers {
    @FXML
    private VBox playersNumberDisplay;
    @FXML
    private Label currentPlayers;
    @FXML
    private Label playersToStart;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private ListView<String> playerList;

    private ObservableList<String> players;

    @FXML
    private void initialize() {
        GUI gui = GUI.instance();

        currentPlayers.textProperty().bind(gui.getModel().currentPlayersProperty().asString());
        playersToStart.textProperty().bind(gui.getModel().playersToStartProperty().asString());

        if(gui.getModel().playersToStartProperty().get() == -1) {
            playersNumberDisplay.setVisible(false);
            gui.getModel().playersToStartProperty().addListener((change, prev, next) -> {
                if (next.intValue() != -1)
                    playersNumberDisplay.setVisible(true);
            });
        }

        players = FXCollections.observableList(new ArrayList<>(gui.getModel().getPlayers().keySet()));
        gui.getModel().getPlayers().addListener((MapChangeListener<String, MockPlayer>) change -> {
            if(change.wasAdded())
                Platform.runLater(() -> players.addAll(change.getKey()));
        });
        playerList.setItems(players);
    }
}
