package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.controller.FXController;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

@FXController(view = "com/midrar/fx/mvc/testapp/fxml/MediaView.fxml")
public class MediaViewController {
    private static final String MEDIA_URL =  MediaViewController.class.getResource("/aicha.mp4").toExternalForm();
    @FXML
    StackPane root;

    @FXML
    private MediaView mediaView;
    @FXML
    private void initialize(){
        // setStartView media player
        Media media = new Media(MEDIA_URL);
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
        //mediaView.fitHeightProperty().bindBidirectional(rootNode.prefHeightProperty());
        //mediaView.fitWidthProperty().bindBidirectional(rootNode.prefWidthProperty());
    }
}
