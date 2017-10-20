package com.midrar.fx.mvc.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Decoration {
    /**
     * Use this constant to define a localised value for the title.
     * This means that the title value is searched in the resource bundle associated
     * of the view.
     */
    public static final String LOCALISED_TITLE = "title";

    /**
     * Defines a title for the {@link View} to use when is shown in a {@link javafx.stage.Stage} or a {@link javafx.scene.control.Tab}.
     */
    String title() default LOCALISED_TITLE;

    /**
     * Defines a list of icon images (.png or .jpg) for the {@link View} to use when is shown in a {@link javafx.stage.Stage} or a {@link javafx.scene.control.Tab}.
     */
    String[] icons() default {};
}
