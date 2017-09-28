package com.midrar.fx.mvc.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to define an action actionHandler for the {@link javafx.event.ActionEvent} on a
 * {@link javafx.scene.control.ButtonBase} or {@link javafx.scene.control.MenuItem} controls.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OnAction {
    String value();
    Class[] parameterTypes() default {};
    String[] parameterValues() default {};
}
