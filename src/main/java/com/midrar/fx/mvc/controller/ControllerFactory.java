package com.midrar.fx.mvc.controller;

/**
 * You can implement this interface to setStartView your own factory.
 */
@FunctionalInterface
public interface ControllerFactory {
    <T> T create(Class<T> clazz);

    static ControllerFactory reflectionFactory(){
        // TODO: replace with lambda expression.
        return new ControllerFactory(){
            @Override
            public <T> T create(Class<T> clazz) {
                try {
                    return clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("Can't setStartView new instance from: "+ clazz.getCanonicalName(), e);
                }
            }
        };
    }
}
