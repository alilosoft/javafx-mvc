package com.midrar.fx.mvc.view;


import com.midrar.fx.mvc.testapp.FXController1;
import com.sun.javafx.application.PlatformImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewFactoryImpTest {

    @Before
    public void setUp() throws Exception {
        PlatformImpl.startup(() -> System.out.println("Startup Platform"));
    }

    @After
    public void tearDown() throws Exception {
        PlatformImpl.exit();
    }

    @Test(expected = NullPointerException.class)
    public void createViewNullControllerClass() {
        Views.create(null);
    }

    @Test(expected = RuntimeException.class)
    public void createViewNoFXControllerAnnotation() throws Exception {
        Views.create(String.class);
    }

    @Test
    public void createView() {
        View view = Views.create(FXController1.class);
        assertThat(view).isNotNull();
        assertThat(view.getController()).isNotNull();
        assertThat(view.getController()).isInstanceOf(FXController1.class);
        assertThat(view.getRootNode()).isNotNull();
    }


}