package com.midrar.fx.mvc.view;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
class ViewContext {

    private Map<Class, View> context = new ConcurrentHashMap<>();

    public View findView(Class controllerClass) {
        return context.get(controllerClass);
    }

    public void registerView(Class controllerClass, View view) {
        System.out.println("registering: " + view + " in: " + this);//TODO: delete me
        context.putIfAbsent(controllerClass, view);
    }
}
