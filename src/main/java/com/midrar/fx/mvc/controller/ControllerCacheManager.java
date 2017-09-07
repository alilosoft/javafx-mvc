package com.midrar.fx.mvc.controller;

public interface ControllerCacheManager {
    void putInCache(Class controllerClass, Object controller);
    <T> T getFromCache(Class<T> controllerClass);
}
