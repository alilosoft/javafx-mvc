package com.midrar.fx.mvc.controller;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class responsible for managing controllers, (instantiating, parsing annotations)
 */
public class ControllerManagerImpl implements ControllerCacheManager {
    private static final ControllerManagerImpl INSTANCE = new ControllerManagerImpl();

    private ControllerFactory controllerFactory;
    private boolean enableCaching = false;
    private Map<Class, Object> controllersCache = new ConcurrentHashMap<>();
    private ControllerCacheManager controllerCacheManager;

    public static ControllerManagerImpl getInstance(){
        return INSTANCE;
    }

    private ControllerManagerImpl() {}

    /**
     * Provide your own factory to this manager if you want a custom instances management.</br>
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
    public <T> T getController(Class<T> controllerClass) {
        if (controllerFactory == null) {
            setControllerFactory(ControllerFactory.reflectionFactory());
        }
        T controller = enableCaching ? getFromCache(controllerClass) : null;
        if (controller == null) {
            controller = controllerFactory.create(controllerClass);
            if (enableCaching) {
                putInCache(controllerClass, controller);
            }
        }
        return controller;
    }

    public void setControllerCacheManager(ControllerCacheManager controllerCacheManager) {
        this.controllerCacheManager = controllerCacheManager;
    }

    @Override
    public <T> T getFromCache(Class<T> controllerClass) {
        if(controllerCacheManager != null){
            return controllerCacheManager.getFromCache(controllerClass);
        }
        return (T) controllersCache.get(controllerClass);
    }

    @Override
    public void putInCache(Class controllerClass, Object controller) {
        if(controllerCacheManager != null){
            controllerCacheManager.putInCache(controllerClass, controller);
        }else{
            controllersCache.put(controllerClass, controller);
        }
    }
}
