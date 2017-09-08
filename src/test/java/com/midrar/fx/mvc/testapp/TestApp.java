package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.view.ViewFactory;
import com.midrar.fx.mvc.view.ViewFactoryImp;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;

public class TestApp extends Application {
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.forLanguageTag("ar"));
        //ControllerManagerImpl controllerManager = ControllerManagerImpl.getInstance();
        //controllerManager.setEnableCaching(false);
        //controllerManager.setControllerFactory(ControllerFactory.reflectionFactory());
        //StageConfigurer stageConfigurer = new StageConfigurer().resizable(false);
        ViewFactory viewFactory = ViewFactoryImp.getInstance();

        viewFactory.createView(FXController1.class).showInNewStage();
        //viewFactory.createView(FXController2.class).showInNewStage();
        //viewFactory.createView(null).showInNewStage();
        //ViewFactoryImp.createView(FXController1.class);

    }
}
