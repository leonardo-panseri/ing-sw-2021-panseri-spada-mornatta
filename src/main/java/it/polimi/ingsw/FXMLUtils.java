package it.polimi.ingsw;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;

public class FXMLUtils {
    public static <T extends Parent> void loadEditorFXML(T component) {
        String fileName = "/editor/" + component.getClass().getSimpleName() + ".fxml";
        loadFXMLComponent(component, fileName);
    }

    public static <T extends Parent> void loadWidgetFXML(T component) {
        String fileName = "/widget/" + component.getClass().getSimpleName() + ".fxml";
        loadFXMLComponent(component, fileName);
    }

    public static <T extends Parent> void loadFXMLComponent(T component, String fileName) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fileName));
        loader.setRoot(component);
        loader.setControllerFactory(theClass -> component);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Parent loadFXML(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return new Label("Something went wrong");
        }
    }
}