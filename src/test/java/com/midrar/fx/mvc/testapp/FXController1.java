package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.controller.PostInjections;
import com.midrar.fx.mvc.controller.ShowView;
import com.midrar.fx.mvc.view.*;
import com.midrar.fx.mvc.controller.FXController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

import java.util.ResourceBundle;


@FXController(fxml = "fxml/view1.fxml")
@I18n("com.midrar.fx.mvc.testapp.i18n.bundle")
@Decoration(title = "FXView 1 Example", icons = {"icons/Home_16px.png", "icons/Home_24px.png"})
@CSS("css/view1-style.css")
public class FXController1 {

    private ViewFactory viewFactory = ViewFactory.getInstance();

    @FXML
    private ResourceBundle resources;

    //@FXView(FXController2.class)
    private View fxView2;

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
        System.out.println("initializing...." + this);
        if(fxView2 == null){
            //fxView2 = viewFactory.createView(FXController2.class);
        }
        System.out.println("fxView2: " + fxView2);

        helloProperty.setValue(resources.getString("hello"));
        helloLabel.textProperty().bindBidirectional(helloProperty);

        helloBtn.setOnAction(this::hello);

        //showView2Btn.disableProperty().bindBidirectional(fxView2.getIsShownInStage());

        showView2Btn.setOnAction(e -> {
            helloProperty.setValue("showView2 clicked: " + ++clickCount);
            fxView2.showInNewStage();
        });
    }

    public void hello(ActionEvent event) {
        helloProperty.setValue("hello() called");
        //helloView.showInNewStage();
    }

    public void hello(String v) {
        System.out.println("hello(String)" + v);
    }
}
