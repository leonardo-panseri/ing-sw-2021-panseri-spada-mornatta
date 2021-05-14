package it.polimi.ingsw.editor;

import it.polimi.ingsw.editor.controller.EditLeaderCard;
import it.polimi.ingsw.server.GameConfig;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        editLeaderCardsPage = FXMLUtils.loadFXML("/editor/EditLeaderCards");
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

    public static void goToEditLeaderCards() {
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
}
