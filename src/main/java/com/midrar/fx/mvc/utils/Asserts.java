package com.midrar.fx.mvc.utils;

import java.util.Objects;

public class Asserts {
    /**
     * Assert that a class have a specific annotation, and return that annotation object if exist.
     *
     * @param clazz
     * @param annotationClass
     * @return the annotation object.
     */
    public static <T> T assertAnnotation(Class clazz, Class<T> annotationClass) {
        T annotation = (T) clazz.getAnnotation(annotationClass);
        if (annotation == null) {
            throw new RuntimeException(clazz.getName() + " must have an annotation of type: " + annotationClass.getName());
        }
        return annotation;
    }

    public static void assertParameterNotNull(Object o, String parameterName) {
        Objects.requireNonNull(o, "You must provide non null parameter for: "+parameterName);
    }
}
