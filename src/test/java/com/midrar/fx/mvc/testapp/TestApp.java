package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.controller.ControllerManager;
import com.midrar.fx.mvc.view.StageConfigurer;
import com.midrar.fx.mvc.view.ViewLoader;
import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Locale;

public class TestApp extends Application {
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.forLanguageTag("ar"));

        ControllerManager controllerManager = new ControllerManager();
        //controllerManager.setEnableCaching(false);
        ViewLoader viewLoader = new ViewLoader(controllerManager);

        StageConfigurer stageConfigurer = new StageConfigurer()
                .resizable(false);

        //viewLoader.loadView(FXController1.class).showInNewStage(stageConfigurer);
        viewLoader.loadView(FxmlController.class).showInNewStage();
        //viewLoader.loadView(FXController2.class).showInNewStage();
    }
}
