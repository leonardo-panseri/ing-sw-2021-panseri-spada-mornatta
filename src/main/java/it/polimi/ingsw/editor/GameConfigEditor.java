package it.polimi.ingsw.editor;

import it.polimi.ingsw.editor.controller.EditBaseProduction;
import it.polimi.ingsw.editor.controller.EditDevelopmentCard;
import it.polimi.ingsw.editor.controller.EditLeaderCard;
import it.polimi.ingsw.server.GameConfig;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
        editFaithTrackPage = FXMLUtils.loadFXML("/editor/EditFaithTrack");

        Font.loadFont(getClass().getResourceAsStream("/fonts/Girassol-Regular.ttf"), 16);

        scene = new Scene(homePage);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void goToHome() {
        scene.setRoot(homePage);
    }

    public static void goToEditLeaderCards()  {
        try {
            editLeaderCardsPage = FXMLUtils.loadFXML("/editor/EditLeaderCards");
        } catch (IOException e) {
            System.err.println("Unable to load leader cards list");
        }
        scene.setRoot(editLeaderCardsPage);
    }

    public static void goToEditDevelopmentCards() {
        try {
            editDevelopmentCardsPage = FXMLUtils.loadFXML("/editor/EditDevelopmentCards");
        } catch (IOException e) {
            System.err.println("Unable to load leader cards list");
        }
        scene.setRoot(editDevelopmentCardsPage);
    }

    public static void goToEditBaseProduction() {
        editBaseProductionPage = new EditBaseProduction();
        scene.setRoot(editBaseProductionPage);
    }

    public static void goToEditFaithTrack() {
        scene.setRoot(editFaithTrackPage);
    }

    public static void goToEditLeaderCard(EditLeaderCard editLeaderCard) {
        scene.setRoot(editLeaderCard);
    }

    public static void goToEditDevelopmentCard(EditDevelopmentCard editDevelopmentCard) {
        scene.setRoot(editDevelopmentCard);
    }

    public static GameConfig getGameConfig() {
        return gameConfig;
    }

    public static void setSavable() {
        VBox commandVBox = (VBox) homePage.getChildrenUnmodifiable().get(homePage.getChildrenUnmodifiable().size()-1);
        commandVBox.getChildren().get(commandVBox.getChildren().size() - 1).setDisable(false);
    }
}
