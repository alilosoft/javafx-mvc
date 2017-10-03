package com.midrar.fx.mvc.view;

import com.midrar.fx.mvc.controller.FXController;
import com.midrar.fx.mvc.controller.OnAction;
import com.midrar.fx.mvc.controller.ShowView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.midrar.fx.mvc.utils.Asserts.assertAnnotation;
import static com.midrar.fx.mvc.utils.Asserts.assertParameterNotNull;

class AnnotationsParser {
    private Object controller;

    AnnotationsParser(Object controller) {
        this.controller = controller;
    }

    /**
     * Parse the fxml file url from the @{@link FXController} annotation.
     * @param controllerClass:
     * @return fxml file url.
     */
    private static URL loadFxmlFileUrl(Class<?> controllerClass) {
        FXController fxAnnotation = assertAnnotation(controllerClass, FXController.class);
        String fxmlFileName = fxAnnotation.fxml();
        // try to load the fxml file.
        URL fxmlUrl = controllerClass.getResource(fxmlFileName);
        if (fxmlUrl == null) {
            throw new RuntimeException("Can't find fxml file: " + fxmlFileName + " defined by @FXController on: " + controllerClass);
        }
        return fxmlUrl;
    }

    /**
     * Get the resource bundle base name if exist, from the @{@link I18n} annotation
     * and return a resource bundle using the default local.
     * @param controllerClass:
     * @return Optional<ResourceBundle>
     */
    private static Optional<ResourceBundle> loadResourceBundle(Class<?> controllerClass) {
        I18n i18nAnnotation = controllerClass.getAnnotation(I18n.class);
        if (i18nAnnotation == null) {
            return Optional.empty();
        }
        String resourceBaseName = i18nAnnotation.value();
        try {
            return Optional.of(ResourceBundle.getBundle(resourceBaseName));
        } catch (Exception e) {
            throw new RuntimeException("Can't setStartView resource bundle: " + resourceBaseName + " defined by: " + controllerClass, e);
        }
    }

    /**
     * Get the title defined by @{@link Decoration} annotation.
     * @param controller:
     * @return the title defined by @{@link Decoration} if any
     */
    static String loadTitle(Object controller) {
        Class<?> controllerClass = controller.getClass();
        Decoration decorationAnnotation = controllerClass.getAnnotation(Decoration.class);
        if (decorationAnnotation == null) {
            return "";
        }
        return decorationAnnotation.title();
    }

    /**
     * Get the icons defined by @{@link Decoration} annotation.
     *
     * @param controller:
     * @return list of icons defined by @{@link Decoration} or {@link Collections.EmptyList} if non.
     */
    static List<Image> loadIcons(Object controller) {
        Class<?> controllerClass = controller.getClass();
        Decoration decorationAnnotation = controllerClass.getAnnotation(Decoration.class);
        if (decorationAnnotation == null) {
            return Collections.EMPTY_LIST;
        }
        String[] icons = decorationAnnotation.icons();
        return Arrays.asList(icons).stream()
                .map(icon -> {
                    URL iconFileUrl = controllerClass.getResource(icon);
                    if (!icon.isEmpty() && iconFileUrl == null) {
                        throw new RuntimeException("Can't load the icon file: " + icon + " defined by: " + controllerClass);
                    }
                    return iconFileUrl;
                })
                .map(url -> new Image(url.toExternalForm()))
                .collect(Collectors.toList());
    }

    /**
     * Get CSS files URLs defined by @{@link CSS} annotation.
     * @param controller:
     */
    static List<String> loadCssUrls(Object controller) {
        Class<?> controllerClass = controller.getClass();
        CSS cssAnnotation = controllerClass.getAnnotation(CSS.class);
        if (cssAnnotation == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(cssAnnotation.value()).stream()
                .map(cssFileName -> {
                    URL cssFileUrl = controllerClass.getResource(cssFileName);
                    if (cssFileUrl == null) {
                        throw new RuntimeException("Can't load the CSS file: " + cssFileName + " defined by: " + controllerClass);
                    }
                    return cssFileUrl;
                })
                .map(cssUrl -> cssUrl.toExternalForm())
                .collect(Collectors.toList());
    }

    /**
     * Create a {@link StageConfigurer} object using options defined by @{@link Stage} annotation.
     * @param controller:
     * @return StageConfigurer object.
     */
    static Optional<StageConfigurer> loadStageConfigurer(Object controller) {
        Class<?> controllerClass = controller.getClass();
        Stage stageAnnotation = controllerClass.getAnnotation(Stage.class);
        if (stageAnnotation == null) {
            return Optional.empty();
        }
        StageConfigurer stageConfigurer = new StageConfigurer()
                .style(stageAnnotation.style())
                .modality(stageAnnotation.modality())
                .alwaysOnTop(stageAnnotation.alwaysOnTop())
                .resizable(stageAnnotation.resizable())
                .maximized(stageAnnotation.maximized())
                .iconified(stageAnnotation.iconified())
                .fullScreen(stageAnnotation.fullScreen())
                .fullScreenExitHint(stageAnnotation.fullScreenExitHint());
        return Optional.of(stageConfigurer);
    }

    // because of some architectural problems the injection features are delayed to later versions.
    // TODO: add support in later versions.
    void injectViews(View toView) {
        Field[] fields = toView.getController().getClass().getDeclaredFields();//TODO: add inherited fields
        Arrays.asList(fields)
                .stream()
                .filter(f -> f.getAnnotation(FXView.class) != null)
                .forEach(field -> {
                    try {
                        if (field.getType() != View.class) {
                            throw new RuntimeException(FXView.class.getName() + " annotation can only used on fields of type " + View.class.getName());
                        }
                        FXView fxViewAnnotation = field.getAnnotation(FXView.class);
                        Class controllerClass = fxViewAnnotation.value();
                        View viewToInject = null;
                        if (controllerClass == FXView.ThisView.class) {
                            viewToInject = toView;
                        } else {
                            //TODO: move to a method for re-utilisation
                            ViewContext viewContext = toView.getViewContext();
                            if (viewContext != null) {
                                viewToInject = viewContext.findView(controllerClass);
                                if (viewToInject == null) {
                                    //viewToInject = createViewInContext(controllerClass, viewContext);
                                }
                            }
                        }
                        System.out.println("injecting: " + viewToInject + " to: " + toView);//TODO: delete me
                        boolean canAccess = field.isAccessible();
                        field.setAccessible(true);// to access private fields
                        field.set(toView.getController(), viewToInject);
                        field.setAccessible(canAccess);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Injection of view: " + field.getName() + " to: " + toView.getController() + " has failed!!!", e);
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
