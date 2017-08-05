package com.midrar.fx.mvc.controller;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerManagerTest {

    ControllerManager controllerManager;

    @Before
    public void setUp() throws Exception {
        controllerManager = new ControllerManager();
    }

    @Test(expected = NullPointerException.class)
    public void setControllerFactory() {
        controllerManager.setControllerFactory(null);
    }


    @Test
    public void createControllerWithCachingEnabled() throws Exception {
        controllerManager.setEnableCaching(true);
        assertThat(controllerManager.createController(TestController.class)).isNotNull();
        TestController instance1 = controllerManager.createController(TestController.class);
        TestController instance2 = controllerManager.createController(TestController.class);
        assertThat(instance1.hashCode()).isEqualTo(instance2.hashCode());

    }

    @Test
    public void createControllerWithCachingDisabled() throws Exception {
        controllerManager.setEnableCaching(false);
        assertThat(controllerManager.createController(TestController.class)).isNotNull();
        TestController instance1 = controllerManager.createController(TestController.class);
        TestController instance2 = controllerManager.createController(TestController.class);
        assertThat(instance1.hashCode()).isNotEqualTo(instance2.hashCode());

    }

}