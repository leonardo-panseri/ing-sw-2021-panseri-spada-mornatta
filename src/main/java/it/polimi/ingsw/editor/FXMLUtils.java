package it.polimi.ingsw.editor;

import it.polimi.ingsw.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FXMLUtils {
    public static <T extends Parent> void loadFXML(T component) {
        String fileName = component.getClass().getSimpleName() + ".fxml";
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fileName));
        loader.setRoot(component);
        loader.setControllerFactory(theClass -> component);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}