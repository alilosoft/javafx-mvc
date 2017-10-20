package com.midrar.fx.mvc.view;

import javafx.event.Event;
import javafx.event.EventHandler;

public interface View<T> {
    T getController();
    void show();
    void hide();
    boolean isShowing();
    void onShown(EventHandler<Event> handler);
    void onHidden(EventHandler<Event> handler);
    void onHideRequest(EventHandler<Event> handler);
}
