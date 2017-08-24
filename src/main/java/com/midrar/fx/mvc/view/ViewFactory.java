package com.midrar.fx.mvc.view;

import com.midrar.fx.mvc.controller.ControllerManager;
import com.midrar.fx.mvc.controller.FXController;
import com.midrar.fx.mvc.controller.OnAction;
import com.midrar.fx.mvc.controller.ShowView;
import com.midrar.fx.mvc.core.FXMLLoaderException;
import com.midrar.fx.mvc.utils.Asserts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.midrar.fx.mvc.utils.Asserts.assertAnnotationExist;

class ViewFactory implements ViewCacheManager {

    private static final ViewFactory INSTANCE = new ViewFactory();

    private static final String CONTROLLER_CLASS_NAME_SUFFIX_CONVENTION = "Controller";
    private static final String DEFAULT_FXML_FILES_EXTENSION = ".fxml";
    private static final String DEFAULT_CSS_FILES_EXTENSION = ".css";
    private String fxmlFilesExtension = DEFAULT_FXML_FILES_EXTENSION;
    private String cssFilesExtension = DEFAULT_CSS_FILES_EXTENSION;

    private FXMLLoader fxmlLoader = new FXMLLoader();
    private ControllerManager controllerManager = ControllerManager.getInstance();

    private ViewCacheManager viewCacheManager;
    private Map<Class, View> viewsCache = new ConcurrentHashMap<>();

    public static ViewFactory getInstance() {
        return INSTANCE;
    }

    private ViewFactory() {
        // define the factory to use by fxmlLoader when the controller is defined in .fxml file by fx:controller.
        fxmlLoader.setControllerFactory(controllerManager::getController);
    }

    /**
     * Reset the fxmlLoader instance to avoid conflicts between different uses.
     */
    private void initFXMLLoader() {
        fxmlLoader.setLocation(null);
        fxmlLoader.setResources(null);
        fxmlLoader.setController(null);
        fxmlLoader.setRoot(null);
    }

    /**
     * Load the rootNode node hierarchy from a given fxml to a {@link Parent} node.
     * Using a default {@link FXMLLoader} defined in {@link ViewFactory}.
     *
     * @param fxmlFile
     * @return a {@link Parent} node.
     */
    public Parent loadFxml(URL fxmlFile) {
        return loadFxml(fxmlFile, null);
    }

    /**
     * Load the rootNode node hierarchy from a given fxml to a {@link Parent} node.
     * Using the given {@link FXMLLoader}.
     *
     * @param fxmlFile
     * @return a {@link Parent} node.
     */
    public Parent loadFxml(URL fxmlFile, ResourceBundle resourceBundle) {
        Asserts.assertParameterNotNull(fxmlFile, "fxmlFile");
        fxmlLoader.setRoot(null);// reset the rootNode to null if the loader has been used previously.
        fxmlLoader.setLocation(fxmlFile);
        fxmlLoader.setResources(resourceBundle);
        try {
            return fxmlLoader.load();
        } catch (Exception e) {
            throw new FXMLLoaderException(fxmlFile, e);
        }
    }

    /**
     * Load the root {@link Node} hierarchy from the .fxml file defined by a controller class.
     * The controller class must be annotated with @{@link FXController}.
     *
     * @param controllerClass the controller class annotated with @{@link FXController}.
     * @return a {@link Parent} {@link Node} hierarchy loaded from the .fxml file.
     */
    public Parent loadRoot(Class<?> controllerClass) {
        // assert that the class is annotated with @FXController
        FXController fxController = assertAnnotationExist(controllerClass, FXController.class);
        // reset the fxmlLoader instance to avoid conflicts between different uses.
        initFXMLLoader();// todo: remove & init in loadFxml()
        // parse the .fxml file url and resource bundle if exist
        URL fxmlUrl = getFxmlFileUrl(controllerClass);
        ResourceBundle bundle = getResourceBundle(controllerClass);
        // if the controller is not defined in the .fxml file then create one and pass it to the fxmlLoader.
        if (!fxController.isDefinedInFxml()) {
            // get a controller instance from the controllerManager and pass it to the fxmlLoader.
            Object controller = getController(controllerClass);
            fxmlLoader.setController(controller);// todo: remove this line and pass the controller as parameter to loadFxml()
        }
        // load the root Node hierarchy from the fxml file.
        return loadFxml(fxmlUrl, bundle);
    }

    /**
     * Create and return a {@link View} object defined by a controller class annotated with @{@link FXController}, and set the
     * values of all its fields (see: {@link View}) then inject any @{@link OnAction}, @{@link ShowView} or @{@link FXView} to
     * the corresponding controller object.
     * @param controllerClass a class annotated with @{@link FXController} that define a {@link View} configuration.
     * @return a {@link View} object.
     */
    private View createView(Class controllerClass) {
        assertAnnotationExist(controllerClass, FXController.class);
        Object controller = this.getController(controllerClass);
        // do the injections before calling loadRoot(), for the injected values to be available
        // to the initialize() method called by FXMLLoader when loading the root node.
        //injectViews(controller);
        // other injections
        //Parent rootNode = this.loadRoot(controllerClass);
        View view = new View(controllerClass);
        view.setController(this.getController(controllerClass));
        view.setRootNode(this.loadRoot(controllerClass));
        view.setTitle(this.getTitle(controllerClass));
        view.setIcons(this.getIcons(controllerClass));
        view.setCssUrls(this.getCssUrls(controllerClass));
        view.setStageConfigurer(this.getStageConfigurer(controllerClass));
        return view;
    }

    public View getView(Class controllerClass) {
        // search for the view in the cache
        View view = getFromCache(controllerClass);
        //if the view not found in the cache, then create new one.
        if (view == null) {
            view = new View(controllerClass);
            // caching the new view is done by its constructor, so don't worry ;)
            //putInCache(controllerClass, view);
        }
        return view;
    }

    private void injectViews(Object toController) {
        Field[] fields = toController.getClass().getDeclaredFields();//TODO: add inherited fields
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
                        View view = getView(controllerClass);
                        System.out.println("inject view of class: " + controllerClass.getSimpleName() + " to: " + field.getName());//TODO: delete me
                        boolean canAccess = field.isAccessible();
                        field.setAccessible(true);// to access private fields
                        field.set(toController, view);
                        field.setAccessible(canAccess);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Injection of view: " + field.getName() + " to controller: " + toController + " has failed!!!", e);
                    }
                });
    }

    // TODO: support menu item and list selection events.
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
                        View view = this.getView(controllerClass);
                        System.out.println("@ShowView: " + view);//TODO: delete me
                        int showMode = field.getAnnotation(ShowView.class).showIn();
                        switch (showMode) {
                            case ShowView.SHOW_IN_NEW_STAGE:
                                buttonBase.setOnAction(event -> {
                                    view.showInNewStage();
                                });
                                break;
                            case ShowView.SHOW_IN_SAME_SCENE:
                                buttonBase.setOnAction(event -> view.showInScene(buttonBase.getScene()));
                                break;
                        }
                        System.out.println(field.getName() + ".onAction: " + buttonBase.getOnAction());
                        field.setAccessible(canAccessField);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Inject @ShowView to " + field.getName() + " in controller: " + controller.getClass() + " has failed!!!", e);
                    }
                });
    }

    private void postInjections(View view) {

    }

    /**
     * Utility class to parse the fxml file url from the @{@link FXController} annotation.
     *
     * @param controllerClass
     * @return fxml file url.
     */
    private URL getFxmlFileUrl(Class<?> controllerClass) {
        // get the value annotation
        FXController fxAnnotation = controllerClass.getAnnotation(FXController.class);
        // get fxml file name value.
        String fxmlFileName = fxAnnotation.fxml();
        if (fxmlFileName.isEmpty()) {
            //Use convention over configuration to deduce the fxml file name.
            fxmlFileName = deduceFxmlName(controllerClass);
        }
        if (!fxmlFileName.endsWith(fxmlFilesExtension)) {
            fxmlFileName += fxmlFilesExtension;
        }
        // try to load the fxml file.
        URL fxmlUrl = controllerClass.getResource(fxmlFileName);
        // if the fxml file not found in the specified location, try to find it by
        // switching the search between the package and the classpath.
        if (fxmlUrl == null) {
            if (fxmlFileName.startsWith("/")) {
                fxmlFileName = fxmlFileName.substring(1);
            } else {
                fxmlFileName = "/" + fxmlFileName;
            }
            fxmlUrl = controllerClass.getResource(fxmlFileName);
            // throw an exception if fxml file is not accessible.
            if (fxmlUrl == null) {
                throw new RuntimeException("Can't find fxml file: " + fxmlFileName + " defined by @FXController on: " + controllerClass);
            }
        }
        return fxmlUrl;
    }

    /**
     * Try to parses the fxml file name from value class name.
     *
     * @param viewClass
     * @return fxml file name.
     */
    private String deduceFxmlName(Class<?> viewClass) {
        String fxmlName = "";
        String viewClassName = viewClass.getSimpleName();
        if (viewClassName.endsWith(CONTROLLER_CLASS_NAME_SUFFIX_CONVENTION)) {
            fxmlName = viewClassName.substring(0, viewClassName.length() - CONTROLLER_CLASS_NAME_SUFFIX_CONVENTION.length());
        }
        return fxmlName += ".fxml";
    }

    /**
     * Use the {@link ControllerManager} instance to get a controller object for the given value class.
     * Note that the {@link ControllerManager} uses by default reflection to create new instances,
     * If you want to use a custom factory you can pass it throw the controller manager.
     * Another notice is that the {@link ControllerManager} uses a caching mechanism by default, that mean
     * that the same object reference is returned if this method is called with the same parameter more than once,
     * If you want override this behaviour you can disable the caching in the controller manager of this class.
     *
     * @param controllerClass
     * @return a controller object of the given value class.
     */
    Object getController(Class<?> controllerClass) {
        return controllerManager.getController(controllerClass);
    }

    /**
     * Get the resource bundle base name if exist, from the @{@link I18n} annotation
     * and return a resource bundle using the default local.
     * @param controllerClass
     * @return
     */
    private ResourceBundle getResourceBundle(Class<?> controllerClass) {
        I18n i18nAnnotation = controllerClass.getAnnotation(I18n.class);
        if (i18nAnnotation == null) {
            return null;
        }
        String resourceBaseName = i18nAnnotation.value();
        try {
            return ResourceBundle.getBundle(resourceBaseName);
        } catch (Exception e) {
            throw new RuntimeException("Can't create resource bundle: " + resourceBaseName + " defined by: " + controllerClass, e);
        }
    }

    /**
     * Get the title defined by @{@link Decoration} annotation.
     *
     * @param controllerClass
     * @return
     */
    String getTitle(Class<?> controllerClass) {
        Decoration decorationAnnotation = controllerClass.getAnnotation(Decoration.class);
        if (decorationAnnotation == null) {
            return "";
        }
        return decorationAnnotation.title();
    }

    /**
     * Get the icons defined by @{@link Decoration} annotation.
     *
     * @param controllerClass
     * @return
     */
    List<Image> getIcons(Class<?> controllerClass) {
        Decoration decorationAnnotation = controllerClass.getAnnotation(Decoration.class);
        if (decorationAnnotation == null) {
            return null;
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
     *
     * @param controllerClass
     */
    List<String> getCssUrls(Class<?> controllerClass) {
        CSS cssAnnotation = controllerClass.getAnnotation(CSS.class);
        if (cssAnnotation == null) {
            return null;
        }
        return Arrays.asList(cssAnnotation.value()).stream()
                .map(cssFileName -> {
                    if (!cssFileName.endsWith(cssFilesExtension)) {
                        cssFileName += cssFilesExtension;
                    }
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
     * Create a {@link StageConfigurer} object using options defined by @{@link Stage} annotation on a controller class.
     *
     * @param controllerClass
     * @return StageConfigurer object.
     */
    StageConfigurer getStageConfigurer(Class<?> controllerClass) {
        Stage stageAnnotation = controllerClass.getAnnotation(Stage.class);
        if (stageAnnotation == null) {
            return null;
        }
        return new StageConfigurer()
                .style(stageAnnotation.style())
                .modality(stageAnnotation.modality())
                .resizable(stageAnnotation.resizable())
                .maximized(stageAnnotation.maximized())
                .fullScreen(stageAnnotation.fullScreen())
                .fullScreenExitHint(stageAnnotation.fullScreenExitHint())
                .alwaysOnTop(stageAnnotation.alwaysOnTop())
                .iconified(stageAnnotation.iconified());
    }

    public void setViewCacheManager(ViewCacheManager viewCacheManager) {
        this.viewCacheManager = viewCacheManager;
    }

    @Override
    public void putInCache(Class controllerClass, View view) {
        if (viewCacheManager != null) {
            viewCacheManager.putInCache(controllerClass, view);
        } else {
            viewsCache.put(controllerClass, view);
        }
    }

    @Override
    public View getFromCache(Class controllerClass) {
        if (viewCacheManager != null) {
            return viewCacheManager.getFromCache(controllerClass);
        }
        return viewsCache.get(controllerClass);
    }
}
