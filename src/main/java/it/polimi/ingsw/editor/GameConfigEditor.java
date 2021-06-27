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

/**
 * Main editor class.
 */
public class GameConfigEditor {
    private static Scene scene;

    private static Parent homePage;

    private static GameConfig gameConfig;

    /**
     * Constructs a new GameConfigEditor initializing the GameConfig as the default one.
     */
    public GameConfigEditor() {
        gameConfig = GameConfig.loadDefaultGameConfig();
    }

    /**
     * Starts the GameConfigEditor app.
     *
     * @param stage the main stage of the app
     */
    public void start(Stage stage) {
        homePage = FXMLUtils.loadFXML("/editor/Home");

        Font.loadFont(GameConfigEditor.class.getResourceAsStream("/fonts/Girassol-Regular.ttf"), 16);

        scene = new Scene(homePage);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Navigates to the home page.
     */
    public static void goToHome() {
        scene.setRoot(homePage);
    }

    /**
     * Navigates to the LeaderCards list.
     */
    public static void goToEditLeaderCards() {
        Parent editLeaderCardsPage = FXMLUtils.loadFXML("/editor/EditLeaderCards");
        scene.setRoot(editLeaderCardsPage);
    }

    /**
     * Navigates to the DevelopmentCards list.
     */
    public static void goToEditDevelopmentCards() {
        Parent editDevelopmentCardsPage = FXMLUtils.loadFXML("/editor/EditDevelopmentCards");
        scene.setRoot(editDevelopmentCardsPage);
    }

    /**
     * Navigates to the base production edit page.
     */
    public static void goToEditBaseProduction() {
        Parent editBaseProductionPage = new EditBaseProduction();
        scene.setRoot(editBaseProductionPage);
    }

    /**
     * Navigates to the faith track edit page.
     */
    public static void goToEditFaithTrack() {
        Parent editFaithTrackPage = FXMLUtils.loadFXML("/editor/EditFaithTrack");
        scene.setRoot(editFaithTrackPage);
    }

    /**
     * Navigates to the LeaderCard edit widget.
     *
     * @param editLeaderCard the widget to edit the leader card
     */
    public static void goToEditLeaderCard(EditLeaderCard editLeaderCard) {
        scene.setRoot(editLeaderCard);
    }

    /**
     * Navigates to the DevelopmentCard edit widget.
     *
     * @param editDevelopmentCard the widget to edit the development card
     */
    public static void goToEditDevelopmentCard(EditDevelopmentCard editDevelopmentCard) {
        scene.setRoot(editDevelopmentCard);
    }

    /**
     * Gets the GameConfig.
     *
     * @return the game config
     */
    public static GameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * Set if the save button is active.
     *
     * @param savable true if the save button should be active, false otherwise
     */
    public static void setSavable(boolean savable) {
        VBox commandVBox = (VBox) homePage.getChildrenUnmodifiable().get(homePage.getChildrenUnmodifiable().size() - 1);
        commandVBox.getChildren().get(commandVBox.getChildren().size() - 1).setDisable(!savable);
    }
}
