package com.midrar.fx.mvc.testapp;

import com.midrar.fx.mvc.controller.FXController;
import com.midrar.fx.mvc.view.I18n;

@FXController(view = "fxml/main.fxml", isControllerInFxml = false)
@I18n("com.midrar.fx.mvc.testapp.i18n.bundle")
public class MainController {
}
