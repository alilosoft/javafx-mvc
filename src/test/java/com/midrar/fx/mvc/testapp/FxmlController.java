package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.controller.FXController;
import com.midrar.fx.mvc.view.CSS;
import com.midrar.fx.mvc.view.Decoration;
import com.midrar.fx.mvc.view.I18n;
import com.midrar.fx.mvc.view.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

@FXController(fxml = "fxml/test.fxml")
@I18n("com.midrar.fx.mvc.testapp.i18n.bundle")// TODO: done
@CSS({"css/test-style.css", "css/view1-style.css"})// TODO: done
@Decoration(title = "fxml fx:controller", icons = {"icons/money48.png", "icons/money24.png", "icons/money16.png"})// TODO:
@Stage(modality = Modality.APPLICATION_MODAL, style = StageStyle.UNIFIED, maximized = true)// TODO: => StageConfigurer
public class FxmlController {
    @FXML
    private Button helloButton;

    @FXML
    private void initialize(){
        System.out.println("initializing..."+ this);
    }

    @FXML
    private void hello(){
        System.out.println(" hello fx:controller");
    }

}
