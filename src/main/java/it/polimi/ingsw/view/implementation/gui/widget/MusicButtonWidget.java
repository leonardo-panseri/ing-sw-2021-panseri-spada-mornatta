package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;

public class MusicButtonWidget extends Button {

    private final boolean isPlayingAtInstantiation;

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
