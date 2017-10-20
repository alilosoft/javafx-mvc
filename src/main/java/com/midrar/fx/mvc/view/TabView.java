package com.midrar.fx.mvc.view;

import javafx.event.Event;
import javafx.event.EventHandler;

class TabView<T> extends ParentView<T> {

    TabView(Class<T> controllerClass) {
        super(controllerClass);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public boolean isShowing() {
        return false;
    }

    @Override
    public void onShown(EventHandler<Event> handler) {

    }

    @Override
    public void onHidden(EventHandler<Event> handler) {

    }

    @Override
    public void onHideRequest(EventHandler<Event> handler) {

    }
}
