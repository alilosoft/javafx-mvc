package com.midrar.fx.mvc.view;

import javafx.event.Event;
import javafx.event.EventHandler;

public interface View<T> {
    T getController();
    void show();
    void hide();
    boolean isShowing();
    void setOnShow(EventHandler<Event> handler);
    void setOnHide(EventHandler<Event> handler);
    void setOnHideRequest(EventHandler<Event> handler);
}
