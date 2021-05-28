package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.model.Resource;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;

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

    public static ClipboardContent getClipboardForResource(Resource resource, ImageView img) {
        ClipboardContent content = new ClipboardContent();
        content.putString(resource.toString());
        content.putImage(img.getImage());
        return content;
    }
}
