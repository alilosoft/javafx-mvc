package com.midrar.fx.mvc.view;

class PaneView<T> extends ParentView<T>{

    PaneView(T controller) {
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
