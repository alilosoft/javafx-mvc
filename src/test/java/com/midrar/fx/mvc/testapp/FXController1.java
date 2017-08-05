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


@FXController(fxml = "fxml/view1.fxml", isDefinedInFxml = false)
@I18n("com.midrar.fx.mvc.testapp.i18n.bundle")
public class FXController1 {

    @FXView
    private View thisView;

    @FXView(controllerClass = FXController2.class)
    private View fxView2;

    @FXView(controllerClass = FxmlController.class)
    private View fxmlView;

    @FXML
    private ResourceBundle resources;

    @FXML
    private TabPane rootTabPane;

    @FXML
    private Pane tab2;

    @FXML
    private Button helloBtn;

    @FXML
    private Button showView2Btn;

    @FXML
    @ShowView(controllerClass = FXController2.class)
    private Button showViewBtn;

    @FXML
    private Label helloLabel;

    @FXML
    private Label viewNameLable;

    @FXML
    private RadioButton radioBtn;

    StringProperty helloProperty = new SimpleStringProperty();

    private int clickCount = 0;

    public void initialize() {
        System.out.println("initializing...." + this);
        System.out.println("resources bundle: " + resources);

        helloProperty.setValue(resources.getString("hello"));
        helloLabel.textProperty().bindBidirectional(helloProperty);

        helloBtn.setOnAction(this::hello);

        showView2Btn.setOnAction(e -> {
            helloProperty.setValue("showView2 clicked: " + ++clickCount);
            //fxView2.addToTabPane(rootTabPane);
        });
    }

    @PostInjections
    private void init() {
        System.out.println("thisView: " + thisView + " fxView2: " + fxView2);
        showView2Btn.disableProperty().bindBidirectional(fxView2.getIsShown());
    }

    public void hello(ActionEvent event) {
        helloProperty.setValue("hello() called");
        //fxmlView.showInNewStage();
    }

    public void hello(String v) {
        System.out.println("hello(String)" + v);
    }
}
