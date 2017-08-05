package com.midrar.fx.mvc.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO: doc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FXView {
    /**
     * TODO: doc
     */
    Class controllerClass() default ThisClass.class;

    /**
     * A constant class used to informs the {@link ViewLoader} to use the same...TODO: doc
     */
    class ThisClass {}
}
