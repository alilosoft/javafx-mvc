package com.midrar.fx.mvc.controller;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerManagerTest {

    ControllerManager controllerManager;

    @Before
    public void setUp() throws Exception {
        controllerManager = ControllerManager.getInstance();
    }

    @Test(expected = NullPointerException.class)
    public void setControllerFactory() {
        controllerManager.setControllerFactory(null);
    }


    @Test
    public void createControllerWithCachingEnabled() throws Exception {
        controllerManager.setEnableCaching(true);
        assertThat(controllerManager.getController(TestController.class)).isNotNull();
        TestController instance1 = controllerManager.getController(TestController.class);
        TestController instance2 = controllerManager.getController(TestController.class);
        assertThat(instance1.hashCode()).isEqualTo(instance2.hashCode());
    }

    @Test
    public void createControllerWithCachingDisabled() throws Exception {
        controllerManager.setEnableCaching(false);
        assertThat(controllerManager.getController(TestController.class)).isNotNull();
        TestController instance1 = controllerManager.getController(TestController.class);
        TestController instance2 = controllerManager.getController(TestController.class);
        assertThat(instance1.hashCode()).isNotEqualTo(instance2.hashCode());
    }

    @Test
    public void usingCustomCacheManager(){
        controllerManager.setControllerCacheManager(new ControllerCacheManager() {
            String cache;
            @Override
            public void putInCache(Class controllerClass, Object controller) {
                cache = (String) controller;
            }

            @Override
            public <T> T getFromCache(Class<T> controllerClass) {
                return (T) cache;
            }
        });
        controllerManager.putInCache(String.class, "alilo");
        assertThat(controllerManager.getFromCache(String.class)).isEqualTo("alilo");
    }

}