package com.midrar.fx.mvc.controller;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerManagerImplTest {

    ControllerManagerImpl controllerManagerImpl;

    @Before
    public void setUp() throws Exception {
        controllerManagerImpl = ControllerManagerImpl.getInstance();
    }

    @Test(expected = NullPointerException.class)
    public void setControllerFactory() {
        controllerManagerImpl.setControllerFactory(null);
    }


    @Test
    public void createControllerWithCachingEnabled() throws Exception {
        controllerManagerImpl.setEnableCaching(true);
        assertThat(controllerManagerImpl.getController(TestController.class)).isNotNull();
        TestController instance1 = controllerManagerImpl.getController(TestController.class);
        TestController instance2 = controllerManagerImpl.getController(TestController.class);
        assertThat(instance1.hashCode()).isEqualTo(instance2.hashCode());
    }

    @Test
    public void createControllerWithCachingDisabled() throws Exception {
        controllerManagerImpl.setEnableCaching(false);
        assertThat(controllerManagerImpl.getController(TestController.class)).isNotNull();
        TestController instance1 = controllerManagerImpl.getController(TestController.class);
        TestController instance2 = controllerManagerImpl.getController(TestController.class);
        assertThat(instance1.hashCode()).isNotEqualTo(instance2.hashCode());
    }

    @Test
    public void usingCustomCacheManager(){
        controllerManagerImpl.setControllerCacheManager(new ControllerCacheManager() {
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
        controllerManagerImpl.putInCache(String.class, "alilo");
        assertThat(controllerManagerImpl.getFromCache(String.class)).isEqualTo("alilo");
    }

}