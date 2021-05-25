package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.model.Resource;
import javafx.scene.image.Image;

import java.io.InputStream;

public class GUIUtils {
    public static Image getResourceImage(Resource resource, double width, double height) {
        InputStream imgIs = GUIUtils.class.getResourceAsStream("/images/resources/" + resource.toString().toLowerCase() + ".png");
        if(imgIs == null) {
            System.err.println("Image not found for " + resource);
            return new Image("");
        }
        return new Image(imgIs, width, height, true, true);
    }
}
