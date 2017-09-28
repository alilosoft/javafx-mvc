package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.view.*;
import com.midrar.fx.mvc.controller.FXController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

import java.util.ResourceBundle;


@FXController(fxml = "fxml/view1.fxml")
@I18n("com.midrar.fx.mvc.testapp.i18n.bundle")
@Decoration(title = "FXView 1 Example", icons = {"icons/Home_16px.png", "icons/Home_24px.png"})
@CSS("css/view1-style.css")
@Stage(resizable = true)
public class FXController1 {
    @FXML
    private ResourceBundle resources;

    //@FXView
    private View thisView;

    //@FXView(FXController2.class)
    private View<FXController2> fxView2;

    //@FXView(FxmlController.class)
    private View helloView;

    @FXML
    private TabPane rootTabPane;

    @FXML
    private Button helloBtn;

    @FXML
    private Button showView2Btn;

    @FXML
    private Label helloLabel;

    private StringProperty helloProperty = new SimpleStringProperty();

    private int clickCount = 0;

    public void initialize() {
        //System.out.println("initializing...." + this);
        //System.out.println("fxView2: " + fxView2);
        //System.out.println("thisView: "+ thisView);


        helloProperty.setValue(resources.getString("hello"));
        helloProperty.bind(helloLabel.textProperty());
        helloBtn.setOnAction(this::hello);



        showView2Btn.setOnAction(e -> {
            thisView = Views.forController(this);
            helloLabel.setText("showView2 clicked: " + ++clickCount);
            if(fxView2 == null){
                fxView2 = Views.create(FXController2.class);
                fxView2.getController().setFxView1(thisView);
            }
            //fxView2.show(thisView.getStage());
            //thisView.close();
            fxView2.show();
        });
    }

    public void hello(ActionEvent event) {
        helloLabel.setText("hello() called");
        if(helloView == null){
            helloView = Views.create(FxmlController.class);
        }
        helloView.show();
    }

    public void hello(String v) {
        System.out.println("hello(String)" + v);
    }

    public void setFxView2(View fxView2) {
        this.fxView2 = fxView2;
    }
}
