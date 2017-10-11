package com.midrar.fx.mvc.view;

import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;

public class StageView<T> extends ParentView<T>{
    private Stage stage;
    private Scene scene;

    StageView(T controller, Stage _stage) {
        super(controller);
        if(_stage == null){
            stage = new Stage();
            annotationsParser.stageConfigurer().ifPresent(this::setStageConfigurer);
        }else {
            stage = _stage;
        }
        scene = new Scene(getRootNode());
        scene.getStylesheets().addAll(getCssUrls());
        if (Locale.getDefault().equals(new Locale("ar"))) {
            scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
        stage.setTitle(getTitle());
        stage.getIcons().clear();
        stage.getIcons().addAll(getIcons());
    }

    void setStageConfigurer(StageConfigurer stageConfigurer) {
        stageConfigurer.configure(stage);
    }

    /**
     * Show this {@link ParentView} in its own {@link Stage}.
     * >Note: if a stage configuration is provided using the @{@link com.midrar.fx.mvc.view.Stage}
     * annotation then it will be applied for the stage used to show this {@link ParentView} .
     */
    @Override
    public void show(){
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Override
    public boolean isShowing(){
        return getRootNode().getScene() != null &&
                getRootNode().getScene().getWindow() != null &&
                getRootNode().getScene().getWindow().isShowing();
    }

    public void close(){
        stage.close();
    }

}
