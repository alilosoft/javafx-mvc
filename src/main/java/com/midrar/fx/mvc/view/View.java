package com.midrar.fx.mvc.view;

public interface View<T> {
    void show();
    void close();
    boolean isShowing();
    T getController();
}
