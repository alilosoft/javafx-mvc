package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.controller.FXController;
import com.midrar.fx.mvc.view.CSS;
import com.midrar.fx.mvc.view.I18n;
import com.midrar.fx.mvc.view.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

@FXController(view = "fxml/splash.fxml", isControllerInFxml = false)
@CSS("css/splash.css")
@Stage(style = StageStyle.TRANSPARENT, alwaysOnTop = true)
public class SplashController {

    @FXML
    private Text status;

    @FXML
    private Label statusLabel;

    private StringProperty statusProperty = new SimpleStringProperty("Starting.....Please wait");

    public void initialize(){
        statusLabel.textProperty().bind(statusProperty);
    }

    public void showStatus(String status){
        System.err.println(status);
        statusProperty.setValue(status);
        //statusLabel.setText(status);
    }

}
