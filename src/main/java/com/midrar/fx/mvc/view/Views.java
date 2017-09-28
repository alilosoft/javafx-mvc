package com.midrar.fx.mvc.view;

import com.midrar.fx.mvc.controller.*;

import java.util.concurrent.ConcurrentHashMap;

public class Views {

    private static ConcurrentHashMap<Integer, View> viewByControllerCache = new ConcurrentHashMap<>();

    private Views() {
    }

    ;//Hide default construction!!!
    private static ViewFactory viewFactory;

    /**
     * If you want to use a custom {@link ViewFactory} other then the default implementation
     * then use this method to do that.
     *
     * @param factory: your {@link ViewFactory} implementation.
     */
    public static void init(ViewFactory factory) {
        viewFactory = factory;
    }

    /**
     * If you want to use a custom controller factory other then the default implementation
     * witch uses reflection then use this method to do that.
     *
     * @param controllerFactory: your {@link ControllerFactory} implementation
     */
    public static void init(ControllerFactory controllerFactory) {
        viewFactory = new ViewFactoryImpl(controllerFactory);
    }

    /**
     * @param controllerClass: a class annotated with @{@link FXController}
     * @return
     */
    public static <T> View<T> create(Class<T> controllerClass) {
        if (viewFactory == null) viewFactory = new ViewFactoryImpl();
        View newView = viewFactory.createView(controllerClass);
        viewByControllerCache.putIfAbsent(newView.getController().hashCode(), newView);
        return newView;
    }

    /**
     * Get the {@link View} instance associated with the given controller.
     * If the {@link View} is
     *
     * @param controller
     * @param <T>
     * @return
     */
    public static <T> View<T> forController(Object controller) {
        View view = viewByControllerCache.get(controller.hashCode());
        if(view == null) view = create(controller.getClass());
        return view;
    }

    /**
     * Despite how many times this method is called, it returns always the same {@link View} instance,
     * the first one created for the given controllerClass parameter.
     *
     * @param controllerClass: a class annotated with @{@link FXController}
     * @param <T>
     * @return
     */
    public static <T> View<T> forClass(Class<T> controllerClass) {
        View v = null;
        return v;
    }

}
