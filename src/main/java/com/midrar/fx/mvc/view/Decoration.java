package com.midrar.fx.mvc.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Decoration {
    /**
     * Defines a title for the {@link View} to use when is shown in a {@link javafx.stage.Stage} or a {@link javafx.scene.control.Tab}.
     */
    String title();

    /**
     * Defines a list of icon images (.png or .jpg) for the {@link View} to use when is shown in a {@link javafx.stage.Stage} or a {@link javafx.scene.control.Tab}.
     */
    String[] icons() default {};
}
