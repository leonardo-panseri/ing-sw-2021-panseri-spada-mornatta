package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.view.View;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends View {
    private Stage stage;
    private static Scene scene;

    public GUI(Client client, Stage stage) {
        super(client);
        this.stage = stage;
    }

    @Override
    public void run() {
        Parent homePage;
        try {
            homePage = FXMLUtils.loadFXML("/gui/Home");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't load home fxml");
            return;
        }

        Font.loadFont(getClass().getResourceAsStream("/fonts/Girassol-Regular.ttf"), 16);

        scene = new Scene(homePage);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
