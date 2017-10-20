package com.midrar.fx.mvc.view;

import com.midrar.fx.mvc.utils.Asserts;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Locale;
import java.util.Optional;

@Data
@Setter(AccessLevel.NONE)
public class StageView<T> extends ParentView<T> {

    private Stage stage;
    private Scene scene;

    StageView(Class<T> controllerClass, Stage _stage) {
        super(controllerClass);
        long start = System.currentTimeMillis();
        scene = new Scene(getRootNode());
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(getCssUrls());
        if (Locale.getDefault().equals(new Locale("ar"))) {
            scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
        System.out.println("init scene in: "+ (System.currentTimeMillis() - start));
        stage = _stage;
    }

    private void setStageConfigurer(StageConfigurer stageConfigurer) {
        stageConfigurer.configure(stage);
    }

    /**
     * Show this {@link View} in {@link Stage}.
     * >Note: If no {@link Stage} was provided to this {@link View} then this method create a new {@link Stage}.
     * > This method should be called in a JavaFX {@link javafx.application.Application} Thread.
     */
    @Override
    public void show() {
        /**
         * The stage initialization is moved to this method from the constructor (earlier versions),
         * to support the creation of Views outside the JavaFX Application Thread. So the now the
         * Views can be created in the init() method.
         */
        if (stage == null) {// create new stage if no one was provided to this View when created.
            stage = new Stage();
            annotationsParser.stageConfigurer().ifPresent(this::setStageConfigurer);
        }
        stage.setTitle(getTitle());
        if(!getIcons().isEmpty()) {// replace the stage icons only if this View has its own icons.
            stage.getIcons().clear();
            stage.getIcons().addAll(getIcons());
        }
        stage.setScene(scene);
        onShownHandler.ifPresent(h -> stage.setOnShown(h::handle));
        onHiddenHandler.ifPresent(h -> stage.setOnHidden(h::handle));
        onHideRequestHandler.ifPresent(h -> stage.setOnCloseRequest(h::handle));
        stage.show();
        stage.toFront();
    }

    public void show(Stage inStage){
        Asserts.assertParameterNotNull(inStage, "inStage");
        stage = inStage;
        show();
    }

    @Override
    public boolean isShowing() {
        return getRootNode().getScene() != null &&
                getRootNode().getScene().getWindow() != null &&
                getRootNode().getScene().getWindow().isShowing();
    }

    public void hide() {
        stage.close();
    }

}
