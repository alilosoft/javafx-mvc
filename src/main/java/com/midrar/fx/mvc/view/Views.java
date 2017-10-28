package com.midrar.fx.mvc.view;

import com.midrar.fx.mvc.controller.*;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static com.midrar.fx.mvc.utils.Asserts.assertParameterNotNull;

public class Views {
    private Views() {
        throw new AssertionError("Sorry! I'm not instantiable");
    } // hide default constructor

    private static ConcurrentHashMap<Integer, View> viewsCache = new ConcurrentHashMap<>();

    /**
     * Create a {@link StageView} instance that shows in its own/specific {@link Stage} when calling its show() method.
     * @param controllerClass: a class annotated with @{@link FXController}.
     * @return new {@link StageView} instance.
     */
    public static <T> StageView<T> create(Class<T> controllerClass) {
        return create(controllerClass, null);
    }

    /**
     * Create a {@link StageView} instance that shows in the given {@link Stage} when calling its show() method.
     * @param controllerClass: a class annotated with @{@link FXController}.
     * @return new {@link StageView} instance.
     */
    public static <T> StageView<T> create(Class<T> controllerClass, Stage stage) {
        StageView stageView = new StageView(controllerClass, stage);
        viewsCache.putIfAbsent(stageView.getController().hashCode(), stageView);
        return stageView;
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
     * Get the {@link View} instance associated with the given controller instance.
     * @param controller
     * @param <T>
     * @return
     */
    public static <T> View<T> forController(Object controller) {
        View view = viewsCache.get(controller.hashCode());
        if(view == null) throw new RuntimeException("No cached View found for controller: "+ controller);
        return view;
    }

    // because of some architectural problems the injection features are delayed to later versions.
    // TODO: add support in later versions.
    void injectViews(Object injectInController) {
        Field[] fields = injectInController.getClass().getDeclaredFields();//TODO: add inherited fields
        Arrays.asList(fields)
                .stream()
                .filter(f -> f.getAnnotation(FXView.class) != null)
                .forEach(field -> {
                    try {
                        if (field.getType() != View.class) {
                            throw new RuntimeException(FXView.class.getName() + " annotation can only used on fields of type " + View.class.getName());
                        }
                        FXView fxViewAnnotation = field.getAnnotation(FXView.class);
                        Class viewToInjectClass = fxViewAnnotation.value();
                        View viewToInjectIn =  forController(injectInController);
                        View viewToInject = null;
                        if (viewToInjectClass == FXView.ThisView.class) {
                            viewToInject = viewToInjectIn;
                        } else {
                            //TODO: move to a method for re-utilisation
                            ViewContext viewContext = null; //viewToInjectIn.getViewContext();
                            if (viewContext != null) {
                                viewToInject = viewContext.findView(viewToInjectClass);
                                if (viewToInject == null) {
                                    viewToInject = createViewInContext(viewToInjectClass, viewContext);
                                }
                            }
                        }
                        System.out.println("injecting: " + viewToInject + " to: " + viewToInjectIn);//TODO: delete me
                        boolean canAccess = field.isAccessible();
                        field.setAccessible(true);// to access private fields
                        field.set(injectInController, viewToInject);
                        field.setAccessible(canAccess);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Injection of view: " + field.getName() + " to: " + injectInController + " has failed!!!", e);
                    }
                });
    }

    // TODO: add support to menu item and list selection events.
    private void injectOnActions(Object controller) {
        System.out.println("inject actions to controller: " + controller.getClass().getName() + " id:" + System.identityHashCode(controller));//TODO: delete me

        Field[] fields = controller.getClass().getDeclaredFields();//TODO: add access inherited fields
        Arrays.asList(fields).stream()
                .filter(f -> f.getAnnotation(OnAction.class) != null)
                .forEach(field -> {
                    try {
                        OnAction onActionAnnotation = field.getAnnotation(OnAction.class);
                        String methodName = onActionAnnotation.value();
                        Class[] parameterTypes = onActionAnnotation.parameterTypes();
                        String[] parameterValues = onActionAnnotation.parameterValues();
                        Method method = controller.getClass().getMethod(methodName, parameterTypes);
                        System.out.println("onAction method found: " + methodName);//TODO: delete me
                        boolean canAccess = field.isAccessible();
                        field.setAccessible(true);// to access private fields
                        ButtonBase buttonBase = (ButtonBase) field.get(controller);
                        buttonBase.setOnAction(e -> {
                            try {
                                method.invoke(controller, (Object[]) parameterValues);
                            } catch (IllegalAccessException | InvocationTargetException e1) {
                                e1.printStackTrace();
                            }
                        });
                        field.setAccessible(canAccess);
                    } catch (IllegalAccessException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
    }

    // TODO: add support in next versions.
    private void injectShowViews(Object controller) {
        System.out.println("inject showViews to controller: " + controller.getClass().getName() + " id:" + System.identityHashCode(controller));//TODO: delete me
        Field[] fields = controller.getClass().getDeclaredFields();//TODO: add access inherited fields
        Arrays.asList(fields).stream()
                .filter(f -> f.getAnnotation(ShowView.class) != null)
                .forEach(field -> {
                    try {
                        boolean canAccessField = field.isAccessible();
                        field.setAccessible(true);// make sure private fields will accessible.
                        ButtonBase buttonBase = (ButtonBase) field.get(controller);
                        MenuItem menuItem;//TODO: add support for MenuItem
                        if (buttonBase.getOnAction() != null) {
                            System.out.println("@ShowView " + field.getName() + " already injected");
                            return;
                        }
                        Class controllerClass = field.getAnnotation(ShowView.class).controllerClass();
                        View view = null;
                        System.out.println("@ShowView: " + view);//TODO: delete me
                        int showMode = field.getAnnotation(ShowView.class).showIn();
                        switch (showMode) {
                            case ShowView.SHOW_IN_NEW_STAGE:
                                buttonBase.setOnAction(event -> {
                                    view.show();
                                });
                                break;
                            case ShowView.SHOW_IN_SAME_STAGE:
                                //buttonBase.setOnAction(event -> view.show("caller view stage"));
                                break;
                        }
                        System.out.println(field.getName() + ".onAction: " + buttonBase.getOnAction());
                        field.setAccessible(canAccessField);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Inject @ShowView to " + field.getName() + " in controller: " + controller.getClass() + " has failed!!!", e);
                    }
                });
    }

}
