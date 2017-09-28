package com.midrar.fx.mvc.view;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
class ViewContext {

    private Map<Class, View> context = new ConcurrentHashMap<>();

    public View findView(Class controllerClass){
        return context.get(controllerClass);
    }

    public void registerView(View view){
        System.out.println("registering: "+view + " in: "+this);//TODO: delete me
        context.putIfAbsent(view.getController().getClass(), view);
    }
}
