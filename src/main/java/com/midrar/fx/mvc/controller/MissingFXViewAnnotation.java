package com.midrar.fx.mvc.controller;

public class MissingFXViewAnnotation extends RuntimeException {
    public MissingFXViewAnnotation(Class aClass) {
        super("Controller class:" + aClass.getSimpleName() + ", doesn't have @" + FXController.class.getSimpleName() + " annotation");
    }
}
