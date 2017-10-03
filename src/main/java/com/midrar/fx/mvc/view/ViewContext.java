package com.midrar.fx.mvc.view;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
class ViewContext {

    private Map<Class, ParentView> context = new ConcurrentHashMap<>();

    public ParentView findView(Class controllerClass){
        return context.get(controllerClass);
    }

    public void registerView(ParentView view){
        System.out.println("registering: "+view + " in: "+this);//TODO: delete me
        context.putIfAbsent(view.getController().getClass(), view);
    }
}
