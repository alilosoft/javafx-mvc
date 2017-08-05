package com.midrar.fx.mvc.view;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Stage {
    StageStyle style() default StageStyle.DECORATED;
    Modality modality() default Modality.NONE;

    boolean iconified() default false;
    boolean maximized() default false;
    boolean resizable() default true;
    boolean alwaysOnTop() default false;
    boolean fullScreen() default false;
    String fullScreenExitHint() default "";
}
