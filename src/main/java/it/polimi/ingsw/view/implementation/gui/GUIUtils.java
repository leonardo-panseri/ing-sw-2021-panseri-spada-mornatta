package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.editor.EditorGUIUtils;
import it.polimi.ingsw.model.Resource;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.Objects;

/**
 * Utils class for common tasks needed by the GUI.
 */
public class GUIUtils {
    /**
     * Gets the Image for a resource. If the given resource is null returns the image indicating any resources.
     *
     * @param resource a game resource
     * @param width the width of the image
     * @param height the height of the image
     * @return the image for the resource
     */
    public static Image getResourceImage(Resource resource, double width, double height) {
        String path = "/images/resources/" + (resource != null ? resource.toString().toLowerCase() : "any") + ".png";
        InputStream imgIs = GUIUtils.class.getResourceAsStream(path);
        if (imgIs == null) {
            System.err.println("Image not found for " + resource);
            return new Image("");
        }
        return new Image(imgIs, width, height, true, true);
    }

    /**
     * Creates the ClipboardContent for the given resource.
     *
     * @param resource the resource
     * @param img the imageview that will use this clipboard
     * @return clipboardcontent containing info about the resource
     */
    public static ClipboardContent getClipboardForResource(Resource resource, ImageView img) {
        ClipboardContent content = new ClipboardContent();
        content.putString(resource.toString());
        content.putImage(img.getImage());
        return content;
    }

    /**
     * Build a box containing an image on the left and a number on the right.
     *
     * @param imageName the path to the image
     * @param quantity the quantity to display to the right of the image
     * @return an hbox containing the image and the quantity
     */
    public static HBox buildResourceDisplay(String imageName, int quantity) {
        HBox box = new HBox();
        box.getStyleClass().add("hbox");
        box.getChildren().add(new Label("" + quantity));

        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        ImageView image = new ImageView(new Image(Objects.requireNonNull(EditorGUIUtils.class.getResourceAsStream("/images/" + imageName + ".png"))));
        image.setFitHeight(36.0);
        image.setFitWidth(23.0);
        image.setPreserveRatio(true);
        imageBox.getChildren().add(image);
        box.getChildren().add(imageBox);
        return box;
    }
}
