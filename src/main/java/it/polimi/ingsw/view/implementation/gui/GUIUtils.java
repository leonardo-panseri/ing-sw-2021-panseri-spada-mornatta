package it.polimi.ingsw.view.implementation.gui;

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

    public static HBox buildResourceDisplay(String imageName, int quantity) {
        HBox box = new HBox();
        box.getStyleClass().add("hbox");
        box.getChildren().add(new Label("" + quantity));

        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        ImageView image = new ImageView(new Image(Objects.requireNonNull(it.polimi.ingsw.editor.GUIUtils.class.getResourceAsStream("/images/" + imageName + ".png"))));
        image.setFitHeight(36.0);
        image.setFitWidth(23.0);
        image.setPreserveRatio(true);
        imageBox.getChildren().add(image);
        box.getChildren().add(imageBox);
        return box;
    }
}
