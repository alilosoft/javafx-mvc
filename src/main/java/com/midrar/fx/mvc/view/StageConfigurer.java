package com.midrar.fx.mvc.view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class StageConfigurer {
    private Optional<StageStyle> style;
    private Optional<Modality> modality;

    private boolean iconified;
    private boolean fullScreen;
    private Optional<String> fullScreenExitHint;

    private boolean alwaysOnTop;
    private boolean maximized;
    private boolean resizable = true;

    public Stage configure(Stage stage){
        style.ifPresent(stage::initStyle);
        modality.ifPresent(stage::initModality);
        stage.setAlwaysOnTop(alwaysOnTop);
        stage.setResizable(resizable);
        stage.setIconified(iconified);
        stage.setMaximized(maximized);
        stage.setFullScreen(fullScreen);
        fullScreenExitHint.filter(s -> !s.isEmpty()).ifPresent(stage::setFullScreenExitHint);
        return stage;
    }

    public StageConfigurer style(StageStyle style){
        this.style = Optional.of(style);
        return this;
    }

    public StageConfigurer modality(Modality modality){
        this.modality = Optional.of(modality);
        return this;
    }

    public StageConfigurer iconified(boolean iconified){
        this.iconified = iconified;
        return this;
    }

    public StageConfigurer maximized(boolean maximized){
        this.maximized = maximized;
        return this;
    }

    public StageConfigurer resizable(boolean resizable){
        this.resizable = resizable;
        return this;
    }

    public StageConfigurer alwaysOnTop(boolean alwaysOnTop){
        this.alwaysOnTop = alwaysOnTop;
        return this;
    }

    public StageConfigurer fullScreen(boolean fullScreen){
        this.fullScreen = fullScreen;
        return this;
    }

    public StageConfigurer fullScreenExitHint(String fullScreenExitHint){
        this.fullScreenExitHint = Optional.of(fullScreenExitHint);
        return this;
    }
}
