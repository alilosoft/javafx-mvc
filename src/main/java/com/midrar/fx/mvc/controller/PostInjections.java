package com.midrar.fx.mvc.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method to run after a {@link com.midrar.fx.mvc.view.View} is loaded and
 * all injections are done to its @{@link FXController}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostInjections {
}
