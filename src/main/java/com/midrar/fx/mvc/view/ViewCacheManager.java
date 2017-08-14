package com.midrar.fx.mvc.view;

public interface ViewCacheManager {
    void putInCache(Class controllerClass, View view);
    View getFromCache(Class controllerClass);
}
