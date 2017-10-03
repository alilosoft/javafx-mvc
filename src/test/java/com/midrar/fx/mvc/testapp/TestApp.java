package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.view.StageConfigurer;
import com.midrar.fx.mvc.view.View;
import com.midrar.fx.mvc.view.Views;
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
        //Views.create(FXController1.class).show(primaryStage);
        long s = System.currentTimeMillis();
        //for(int i = 0; i < 50; i++)
        View v = Views.create(FXController1.class, primaryStage);
        v.show();
        View v2 = Views.create(FXController2.class, primaryStage);
        //v2.show();
        System.out.println("takes: "+ (System.currentTimeMillis()-s));

        //fxView1.show(primaryStage);
        //fxView1.show(new Stage());
        //viewFactory.createView(FXController2.class).show();
        //viewFactory.createView(null).show();
        //Views.createView(FXController1.class);

    }
}
