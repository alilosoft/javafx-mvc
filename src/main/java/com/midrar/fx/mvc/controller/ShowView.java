package com.midrar.fx.mvc.controller;

import com.midrar.fx.mvc.view.View;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation for {@link javafx.scene.control.ButtonBase} or {@link javafx.scene.control.MenuItem}  to define
 * a {@link View} to show when an {@link javafx.event.ActionEvent} is fired by the annotated control.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ShowView {//TODO: usage
    /**
     * Show the defined {@link View} in new {@link javafx.stage.Stage}.
     */
    public static final int SHOW_IN_NEW_STAGE = 0;
    /**
     * Show the defined view in the same {@link javafx.stage.Stage} as this {@link View}.
     */
    public static final int SHOW_IN_SAME_STAGE = 1;

    /**
     * Specify a view defined by @{@link FXController} annotation to show when an {@link javafx.event.ActionEvent}
     * is fired by the annotated {@link javafx.scene.control.ButtonBase} or {@link javafx.scene.control.MenuItem}.
     */
    Class controllerClass();

    /**
     * Define how the defined {@link View} is shown, SHOW_IN_NEW_STAGE or SHOW_IN_SAME_STAGE
     */
    int showIn() default SHOW_IN_NEW_STAGE;

}
