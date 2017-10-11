package com.midrar.fx.mvc.testapp;


import com.midrar.fx.mvc.controller.FXController;
import com.midrar.fx.mvc.view.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

@FXController(view = "fxml/view2.fxml", isControllerInFxml = false)
@CSS("css/view2-style.css")
@I18n("com.midrar.fx.mvc.testapp.i18n.bundle")
@Decoration(title = "FXView 2 Example", icons = {"icons/Shopping Cart_16px.png", "icons/Shopping Cart_24px.png"})
@Stage(resizable = false)
public class FXController2 {
    @FXML
    private Pane root;

    //@FXView(FXController1.class)
    private View fxView1;

    //@FXView(FxmlController.class)
    private View helloView;

    @FXML
    private Button showView1Btn;

    @FXML
    private  Button helloBtn;

    public Pane getRoot() {
        return root;
    }

    public void initialize(){
        System.out.println("initializing...." + this);
        System.out.println("fxView1: " + fxView1);
        //helloBtn.setOnAction(e -> helloView.show());
        showView1Btn.setOnAction(e-> {
            if(fxView1 == null){
                System.out.println("creating new view for: "+FXController1.class);
                fxView1 = Views.create(FXController1.class);
            }
            fxView1.show();
        });
    }

    public void setFxView1(View fxView1) {
        this.fxView1 = fxView1;
    }
}
