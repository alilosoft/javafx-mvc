package com.midrar.fx.mvc.testapp;


import com.midrar.fx.mvc.controller.FXController;
import com.midrar.fx.mvc.view.CSS;
import com.midrar.fx.mvc.view.Decoration;
import com.midrar.fx.mvc.view.I18n;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

@FXController(fxml = "fxml/view2.fxml", isDefinedInFxml = false)
@CSS("css/view2-style.css")
@I18n("com.midrar.fx.mvc.testapp.i18n.bundle")
@Decoration(title = "View 2 Example", icons = {"icons/Shopping Cart_16px.png", "icons/Shopping Cart_24px.png"})
public class FXController2 {
    @FXML
    private Pane stackPane;

    @FXML
    public TabPane tabPane;

    @FXML
    //@ShowView(controllerClass = FXController1.class)
    private Button showViewBtn;

    @FXML
    private  Button helloBtn;

    public Pane getStackPane() {
        return stackPane;
    }

    public void initialize(){
        System.out.println("initializing..."+ this);
    }
}
