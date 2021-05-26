package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.model.Resource;
import javafx.scene.image.Image;

import java.io.InputStream;

public class GUIUtils {
    public static Image getResourceImage(Resource resource, double width, double height) {
        String path = "/images/resources/" + (resource != null ? resource.toString().toLowerCase() : "any") + ".png" ;
        InputStream imgIs = GUIUtils.class.getResourceAsStream(path);
        if(imgIs == null) {
            System.err.println("Image not found for " + resource);
            return new Image("");
        }
        return new Image(imgIs, width, height, true, true);
    }
}
