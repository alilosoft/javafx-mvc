package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.controller.ControllerFactory;
import com.midrar.fx.mvc.controller.ControllerManager;
import com.midrar.fx.mvc.view.StageConfigurer;
import com.midrar.fx.mvc.view.View;
import com.midrar.fx.mvc.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;

public class TestApp extends Application {
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.forLanguageTag("ar"));
        //ControllerManager controllerManager = ControllerManager.getInstance();
        //controllerManager.setEnableCaching(false);
        //controllerManager.setControllerFactory(ControllerFactory.reflectionFactory());
        //StageConfigurer stageConfigurer = new StageConfigurer().resizable(false);
        ViewFactory viewFactory = ViewFactory.getInstance();

        viewFactory.createView(FXController1.class).showInNewStage();
        //viewFactory.createView(FXController2.class).showInNewStage();
        //viewFactory.createView(null).showInNewStage();
        //ViewFactory.createView(FXController1.class);

    }
}
