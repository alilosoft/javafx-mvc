package com.midrar.fx.mvc.utils;

import com.midrar.fx.mvc.controller.FXController;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertsTest {
    @Test(expected = RuntimeException.class)
    public void assertAnnotationExistNegatif() throws Exception {
        Asserts.assertAnnotationExist(Class.class, FXController.class);
    }

    @Test
    public void assertAnnotationExistPositif() throws Exception {
        assertThat(Asserts.assertAnnotationExist(TestClass.class, FXController.class)).isNotNull();

    }

}
@FXController
class TestClass{
}