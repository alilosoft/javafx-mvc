package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.controller.FXController;
import com.midrar.fx.mvc.view.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;

@FXController(fxml = "fxml/test.fxml")
@I18n("com.midrar.fx.mvc.testapp.i18n.bundle")// TODO: done
@CSS({"css/test-style.css", "css/test-style.css"})// TODO: done
@Decoration(title = "fxml fx:controller", icons = {"icons/money48.png", "icons/money24.png", "icons/money16.png"})
@Stage(style = StageStyle.UNIFIED)
public class FxmlController {

    @FXView(FXController1.class)
    private View fxView1;

    @FXML
    private Button helloButton;

    @FXML
    private void initialize(){
        System.out.println("initializing..."+ this);
    }

    @FXML
    private void hello(){
        System.out.println(" hello fx:controller");
        fxView1.showInStage();
    }



}
