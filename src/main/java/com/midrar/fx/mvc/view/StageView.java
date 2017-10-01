package com.midrar.fx.mvc.view;

import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;

import static com.midrar.fx.mvc.utils.Asserts.*;

public class StageView<T> extends View<T>{
    private Stage stage;

    public StageView(T controller) {
        this(controller ,new Stage());
    }

    public StageView(T controller, Stage stage) {
        super(controller);
        assertParameterNotNull(stage, "stage");
        this.stage = stage;

        Scene scene = new Scene(getRootNode());
        scene.getStylesheets().addAll(getCssUrls());
        if (Locale.getDefault().equals(new Locale("ar"))) {
            scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
        stage.setScene(scene);
        stage.setTitle(getTitle());
        stage.getIcons().clear();
        stage.getIcons().addAll(getIcons());
        ViewLoader.loadStageConfigurer(controller.getClass()).ifPresent(this::setStageConfigurer);
    }

    void setStageConfigurer(StageConfigurer stageConfigurer) {
        stageConfigurer.configure(stage);
    }

    @Override
    public void show(){
        stage.show();
        stage.toFront();
    }

    private boolean isShowing(){
        return getRootNode().getScene() != null &&
                getRootNode().getScene().getWindow() != null &&
                getRootNode().getScene().getWindow().isShowing();
    }

    public void close(){
        stage.close();
    }

}
