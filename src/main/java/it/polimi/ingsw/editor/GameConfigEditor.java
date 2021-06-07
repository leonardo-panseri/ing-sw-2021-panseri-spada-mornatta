package it.polimi.ingsw.editor;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.editor.controller.EditBaseProduction;
import it.polimi.ingsw.editor.controller.EditDevelopmentCard;
import it.polimi.ingsw.editor.controller.EditLeaderCard;
import it.polimi.ingsw.server.GameConfig;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameConfigEditor {
    private static Scene scene;

    private static Parent homePage;

    private static GameConfig gameConfig;

    public GameConfigEditor() {
        gameConfig = GameConfig.loadDefaultGameConfig();
    }

    public void start(Stage stage) {
        homePage = FXMLUtils.loadFXML("/editor/Home");

        Font.loadFont(GameConfigEditor.class.getResourceAsStream("/fonts/Girassol-Regular.ttf"), 16);

        scene = new Scene(homePage);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void goToHome() {
        scene.setRoot(homePage);
    }

    public static void goToEditLeaderCards() {
        Parent editLeaderCardsPage = FXMLUtils.loadFXML("/editor/EditLeaderCards");
        scene.setRoot(editLeaderCardsPage);
    }

    public static void goToEditDevelopmentCards() {
        Parent editDevelopmentCardsPage = FXMLUtils.loadFXML("/editor/EditDevelopmentCards");
        scene.setRoot(editDevelopmentCardsPage);
    }

    public static void goToEditBaseProduction() {
        Parent editBaseProductionPage = new EditBaseProduction();
        scene.setRoot(editBaseProductionPage);
    }

    public static void goToEditFaithTrack() {
        Parent editFaithTrackPage = FXMLUtils.loadFXML("/editor/EditFaithTrack");
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

    public static void setSavable(boolean savable) {
        VBox commandVBox = (VBox) homePage.getChildrenUnmodifiable().get(homePage.getChildrenUnmodifiable().size() - 1);
        commandVBox.getChildren().get(commandVBox.getChildren().size() - 1).setDisable(!savable);
    }
}
