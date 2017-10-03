package com.midrar.fx.mvc.view;

import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;

import static com.midrar.fx.mvc.utils.Asserts.*;

class StageView<T> extends View<T>{
    private Stage stage;
    Scene scene;

    public StageView(T controller) {
        this(controller ,new Stage());
    }

    StageView(T controller, Stage stage) {
        super(controller);
        assertParameterNotNull(stage, "stage");
        this.stage = stage;
        scene = new Scene(getRootNode());
        scene.getStylesheets().addAll(getCssUrls());
        if (Locale.getDefault().equals(new Locale("ar"))) {
            scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
        //stage.setScene(scene);
        stage.setTitle(getTitle());
        stage.getIcons().clear();
        stage.getIcons().addAll(getIcons());
        ViewLoader.loadStageConfigurer(controller.getClass()).ifPresent(this::setStageConfigurer);
    }

    void setStageConfigurer(StageConfigurer stageConfigurer) {
        stageConfigurer.configure(stage);
    }

    /**
     * Show this {@link View} in its own {@link Stage}.
     * >Note: if a stage configuration is provided using the @{@link com.midrar.fx.mvc.view.Stage}
     * annotation then it will be applied for the stage used to show this {@link View} .
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
