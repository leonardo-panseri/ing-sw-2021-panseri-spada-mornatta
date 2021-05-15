package it.polimi.ingsw.editor;

import it.polimi.ingsw.editor.controller.EditLeaderCard;
import it.polimi.ingsw.server.GameConfig;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GameConfigEditor {
    private final Application app;
    private static Scene scene;

    private static Parent homePage;
    private static Parent editLeaderCardsPage;
    private static Parent editDevelopmentCardsPage;
    private static Parent editBaseProductionPage;
    private static Parent editFaithTrackPage;

    private static GameConfig gameConfig;

    public GameConfigEditor(Application app) {
        this.app = app;
        gameConfig = GameConfig.loadDefaultGameConfig();
    }

    public void start(Stage stage) throws Exception {
        homePage = FXMLUtils.loadFXML("/editor/Home");
        editDevelopmentCardsPage = FXMLUtils.loadFXML("/editor/EditDevelopmentCards");
        editBaseProductionPage = FXMLUtils.loadFXML("/editor/EditBaseProduction");
        editFaithTrackPage = FXMLUtils.loadFXML("/editor/EditFaithTrack");

        scene = new Scene(homePage);
        stage.setScene(scene);
        stage.show();
    }

    public static void goToHome() {
        scene.setRoot(homePage);
    }

    public static void goToEditLeaderCards()  {
        try{
            editLeaderCardsPage = FXMLUtils.loadFXML("/editor/EditLeaderCards");
        }catch (IOException e) {
            System.out.println("Unable to load leader cards list");
        }
        scene.setRoot(editLeaderCardsPage);
    }

    public static void goToEditDevelopmentCards() {
        scene.setRoot(editDevelopmentCardsPage);
    }

    public static void goToEditBaseProduction() {
        scene.setRoot(editBaseProductionPage);
    }

    public static void goToEditFaithTrack() {
        scene.setRoot(editFaithTrackPage);
    }

    public static void goToEditLeaderCard(EditLeaderCard editLeaderCard) {
        scene.setRoot(editLeaderCard);
    }

    public static GameConfig getGameConfig() {
        return gameConfig;
    }

    public static void setSavable() {
        VBox commandVBox = (VBox) homePage.getChildrenUnmodifiable().get(homePage.getChildrenUnmodifiable().size()-1);
        commandVBox.getChildren().get(commandVBox.getChildren().size() - 1).setDisable(false);
    }

    public static TextFormatter<String> getNumberInputTextFormatter() {
        return new TextFormatter<>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        });
    }
}
