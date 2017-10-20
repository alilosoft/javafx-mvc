package com.midrar.fx.mvc.view;

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
}
