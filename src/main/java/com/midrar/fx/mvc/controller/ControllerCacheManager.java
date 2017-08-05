package com.midrar.fx.mvc.controller;

public interface ControllerCacheManager {
    void cashController(Class controllerClass, Object controller);
    <T> T getCachedController(Class<T> controllerClass);
}
