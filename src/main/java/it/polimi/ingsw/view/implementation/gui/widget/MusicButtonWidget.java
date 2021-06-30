package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;

/**
 * Widget for the button to play/stop background music.
 */

public class MusicButtonWidget extends Button {

    private final boolean isPlayingAtInstantiation;

    /**
     * Creates a new music button with visuals depending on if music is playing or not.
     * @param isPlayingAtInstantiation true if music was playing at the moment of creation
     */
    public MusicButtonWidget(boolean isPlayingAtInstantiation) {
        this.isPlayingAtInstantiation = isPlayingAtInstantiation;
        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    public void initialize() {
        if (isPlayingAtInstantiation) {
            this.getStyleClass().add("music-button");
        } else this.getStyleClass().add("music-button-pause");
    }

    /**
     * Play/pause music and change the visuals.
     */
    public void toggleMusic() {
        MediaPlayer mediaPlayer = GUI.instance().getMediaPlayer();
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            this.getStyleClass().remove("music-button");
            this.getStyleClass().add("music-button-pause");
        } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
            this.getStyleClass().remove("music-button-pause");
            this.getStyleClass().add("music-button");
        }
    }
}
