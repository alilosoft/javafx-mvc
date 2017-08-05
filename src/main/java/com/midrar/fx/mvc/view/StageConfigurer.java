package com.midrar.fx.mvc.view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.*;

import java.util.List;


public class StageConfigurer {
    private StageStyle style;
    private Modality modality;

    private String title;
    private List<Image> icons;


    private boolean iconified;
    private boolean fullScreen;
    private String fullScreenExitHint;

    private boolean alwaysOnTop;
    private boolean maximized;
    private boolean resizable = true;

    public Stage configure(Stage stage){
        if(style != null) stage.initStyle(style);
        if(modality != null) stage.initModality(modality);
        if(title != null) stage.setTitle(title);
        if(icons != null) stage.getIcons().addAll(icons);
        stage.setAlwaysOnTop(alwaysOnTop);
        stage.setResizable(resizable);
        stage.setIconified(iconified);
        stage.setMaximized(maximized);
        stage.setFullScreen(fullScreen);
        if(fullScreenExitHint != null && !fullScreenExitHint.isEmpty()) {
            stage.setFullScreenExitHint(fullScreenExitHint);
        }
        return stage;
    }

    public StageConfigurer style(StageStyle style){
        this.style = style;
        return this;
    }

    public StageConfigurer modality(Modality modality){
        this.modality = modality;
        return this;
    }

    public StageConfigurer title(String title){
        this.title = title;
        return this;
    }

    public StageConfigurer icons(List<Image> icons){
        this.icons = icons;
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
        this.fullScreenExitHint = fullScreenExitHint;
        return this;
    }
}
