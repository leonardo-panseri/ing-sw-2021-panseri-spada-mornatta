package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.server.GameConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.*;

public class Home {
    @FXML
    private Button saveButton;

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
    public void saveConfig() {
        File config = new File("custom_config_" + System.currentTimeMillis() + ".json");
        try{
            if(!config.createNewFile()) {
                System.out.println("File already existing.");
            }
            else {
                BufferedWriter writer = new BufferedWriter(new FileWriter(config));
                writer.write(GameConfig.serialize(GameConfigEditor.getGameConfig()));
                writer.close();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Ooops, unable to save custom configuration!");

            alert.showAndWait();
            return;
        }

        GameConfigEditor.setSavable(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Configuration saved to path: " + config.getAbsolutePath());

        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        saveButton.setDisable(true);
    }
}
