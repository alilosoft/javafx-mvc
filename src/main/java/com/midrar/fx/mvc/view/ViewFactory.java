package com.midrar.fx.mvc.view;

public interface ViewFactory {
    <T> View<T> createView(Class<T> controllerClass);
}
