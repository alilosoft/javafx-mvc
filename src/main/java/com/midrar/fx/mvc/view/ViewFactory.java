package com.midrar.fx.mvc.view;

import java.util.ResourceBundle;

public interface ViewFactory {
    <T> View<T> createView(Class<T> controllerClass);

    //TODO
    //<T> View<T> createView(String fxmlFile);
    //<T> View<T> createView(String fxmlFile, ResourceBundle bundle);
}
