package com.midrar.fx.mvc.controller;

public class Controllers {
    private Controllers() {
        throw new AssertionError("Sorry! I'm not instantiable");
    } // hide default constructor

    private static ControllerFactory _controllerFactory = ControllerFactory.reflectionFactory();

    /**
     * If you want to use a custom controller factory other then the default implementation
     * -witch uses reflection- then use this method to provide your {@link ControllerFactory} impl.
     * @param controllerFactory: a {@link ControllerFactory} implementation
     */
    public static void init(ControllerFactory controllerFactory) {
        _controllerFactory = controllerFactory;
    }

    public static <T> T create(Class<T> clazz){
        return _controllerFactory.create(clazz);
    }

}
