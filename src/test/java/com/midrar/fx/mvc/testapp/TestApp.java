package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.view.StageView;
import com.midrar.fx.mvc.view.View;
import com.midrar.fx.mvc.view.Views;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;

public class TestApp extends Application {

    public void start(Stage primaryStage) throws Exception {
        //Locale.setDefault(Locale.forLanguageTag("ar"));
        //ControllerManagerImpl controllerManager = ControllerManagerImpl.getInstance();
        //controllerManager.setEnableCaching(false);
        //controllerManager.setControllerFactory(ControllerFactory.reflectionFactory());
        //StageConfigurer stageConfigurer = new StageConfigurer().resizable(false);
        //Views.create(FXController1.class).show(primaryStage);
        long s = System.currentTimeMillis();
        //Views.create(MainController.class).show();
        //for(int i = 0; i < 50; i++)
        StageView<FXController1> fxView1 = Views.create(FXController1.class);
        View<FXController2> fxView2 = Views.create(FXController2.class);
        fxView1.getController().setFxView2(fxView2);
        fxView2.getController().setFxView1(fxView1);

        fxView1.setOnShow(System.out::println);
        fxView1.show();
        fxView1.setOnHide(e ->  System.out.println(e));
        //fxView2.show();
        System.out.println("takes: "+ (System.currentTimeMillis()-s));

        //fxView1.show(primaryStage);
        //fxView1.show(new Stage());
        //viewFactory.createView(FXController2.class).show();
        //viewFactory.createView(null).show();
        //Views.createView(FXController1.class);

    }
}
