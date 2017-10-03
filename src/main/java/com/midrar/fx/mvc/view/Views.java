package com.midrar.fx.mvc.view;

import com.midrar.fx.mvc.controller.*;
import javafx.stage.Stage;

import java.util.concurrent.ConcurrentHashMap;

import static com.midrar.fx.mvc.utils.Asserts.assertParameterNotNull;

public class Views {
    private static ControllerFactory controllerFactory = ControllerFactory.reflectionFactory();
    private static ConcurrentHashMap<Integer, View> viewByControllerCache = new ConcurrentHashMap<>();

    private Views() {
        //Hide the default constructor!!!
    }

    /**
     * If you want to use a custom controller factory other then the default implementation
     * -witch uses reflection- then use this method to provide your {@link ControllerFactory} impl.
     * @param controllerFactory: your {@link ControllerFactory} implementation
     */
    public static void init(ControllerFactory controllerFactory) {
        Views.controllerFactory = controllerFactory;
    }

    /**
     * Create a {@link ParentView} object
     * @param controllerClass: a class annotated with @{@link FXController}
     * @return
     */
    public static <T> View<T> create(Class<T> controllerClass) {
        return create(controllerClass, null);
    }

    public static <T> View<T> create(Class<T> controllerClass, Stage stage) {
        T controller = controllerFactory.create(controllerClass);
        View newView = new StageView(controller, stage);
        viewByControllerCache.putIfAbsent(newView.getController().hashCode(), newView);
        return newView;
    }

    //TODO: this method is intended to support @FXView injection (for later versions)
    private <T> View<T> createViewInContext(Class<T> controllerClass, ViewContext viewContext) {
        assertParameterNotNull(controllerClass, "controllerClass");
        assertParameterNotNull(viewContext, "viewContext");
        View view = create(controllerClass);
        //view.setViewContext(viewContext);
        return view;
    }

    /**
     * Get the {@link View} instance associated with the given controller.
     * If the {@link View} is
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
     * Despite how many times this method is called, it returns always the same {@link ParentView} instance,
     * the first one created for the given controllerClass parameter.
     * @param controllerClass: a class annotated with @{@link FXController}
     * @param <T>
     * @return
     */
    public static <T> View<T> forClass(Class<T> controllerClass) {
        View v = null;//TODO: complete me
        return v;
    }

}
