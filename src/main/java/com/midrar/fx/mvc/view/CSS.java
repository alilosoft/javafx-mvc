package com.midrar.fx.mvc.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a CSS files to add to {@link javafx.scene.Scene} or {@link javafx.scene.control.TabPane}
 * when showing this {@link View}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CSS {
    String[] value();
}
