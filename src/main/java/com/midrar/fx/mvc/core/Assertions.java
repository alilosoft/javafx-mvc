package com.midrar.fx.mvc.core;

public class Assertions {
    public static boolean assertAnnotation(Class clazz, Class annotation){
        return clazz.getAnnotation(annotation) != null;
    }
}
