package com.midrar.fx.mvc.controller;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class responsible for managing controllers, (instantiating, parsing annotations)
 */
public class ControllerManager implements ControllerCacheManager{
    //TODO: search for @FXView annotation and inject the controller instance to the controllerClass instance and vice versa.
    private ControllerFactory controllerFactory;
    private boolean enableCaching = true;
    private Map<Class, Object> controllersCache = new ConcurrentHashMap<>();

    /**
     * Provide your own factory to this manager if you want a custom instance management.</br>
     * The {@link ControllerFactory} is a {@link FunctionalInterface}, so you can pass a lambda or a method reference here.
     * for example: if you use a dependency management framework you can pass a the method responsible for creating or getting beans.
     * @param controllerFactory
     */
    public void setControllerFactory(ControllerFactory controllerFactory) {
        Objects.requireNonNull(controllerFactory, "The controller factory can't be null.");
        this.controllerFactory = controllerFactory;
    }

    private ControllerFactory getControllerFactory() {
        if (controllerFactory == null) {
            controllerFactory = ControllerFactory.reflectionFactory();
        }
        return controllerFactory;
    }

    /**
     * Whether to support caching of created controller instances or not.
     *
     * @param enableCaching
     */
    public void setEnableCaching(boolean enableCaching) {
        this.enableCaching = enableCaching;
    }

    /**
     * Create a controller instance using a {@link ControllerFactory}.</br>
     * If no {@link ControllerFactory} explicitly specified then a default factory that uses reflection will be used.
     * Note that if the caching is enabled (the default) then the same reference is returned if this method is re-called
     * with the same parameter. You can turn of caching by calling setEnableCaching(false) method.
     * @param controllerClass
     * @param <T>             Type of the controller
     * @return a controller instance.
     */
    public <T> T createController(Class<T> controllerClass) {
        if (controllerFactory == null) {
            setControllerFactory(ControllerFactory.reflectionFactory());
        }
        T controller = enableCaching ? getCachedController(controllerClass) : null;
        if (controller == null) {
            controller = controllerFactory.create(controllerClass);
            if (enableCaching) {
                cashController(controllerClass, controller);
            }
        }
        return controller;
    }

    @Override
    public <T> T getCachedController(Class<T> controllerClass) {
        return (T) controllersCache.get(controllerClass);
    }

    @Override
    public void cashController(Class controllerClass, Object controller) {
        controllersCache.putIfAbsent(controllerClass, controller);
    }
}
