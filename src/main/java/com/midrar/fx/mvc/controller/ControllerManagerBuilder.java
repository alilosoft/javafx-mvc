package com.midrar.fx.mvc.controller;

public class ControllerManagerBuilder {
    private ControllerManagerBuilder builder = new ControllerManagerBuilder();
    private ControllerManager controllerManager = new ControllerManager();

    public ControllerManagerBuilder controllerFactory(ControllerFactory controllerFactory){
        controllerManager.setControllerFactory(controllerFactory);
        return this;
    }

    public ControllerManager build(){
        return controllerManager;
    }

}
