package com.midrar.fx.mvc.view;

class PaneView<T> extends ParentView<T>{

    PaneView(Class<T> controllerClass) {
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
}
