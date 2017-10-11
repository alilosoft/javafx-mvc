package com.midrar.fx.mvc.view;

class TabView<T> extends ParentView<T> {
    TabView(T controller) {
        super(controller);
    }

    @Override
    public void show() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isShowing() {
        return false;
    }
}
