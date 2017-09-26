package com.midrar.fx.mvc.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO: add support in next version
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FXView {
    /**
     * TODO: doc
     */
    Class value() default ThisView.class;

    class ThisView{}
}
