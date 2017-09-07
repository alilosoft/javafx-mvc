package com.midrar.fx.mvc.view;


import com.midrar.fx.mvc.testapp.FXController1;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewFactoryTest {

    ViewFactory viewFactory = ViewFactory.getInstance();

    @Before
    public void setUp() throws Exception {
        PlatformImpl.startup(() -> {});
        assertThat(viewFactory).isNotNull();
    }

    @After
    public void tearDown() throws Exception {
        PlatformImpl.exit();
    }

    @Test
    public void createView() {
        View view = viewFactory.createView(FXController1.class);
        assertThat(view).isNotNull();
        assertThat(view.getRootNode()).isNotNull();
    }

    @Test(expected = NullPointerException.class)
    public void createViewWithNullController() {
        viewFactory.createView(null);
    }
}