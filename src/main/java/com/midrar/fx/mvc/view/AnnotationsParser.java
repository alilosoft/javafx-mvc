package com.midrar.fx.mvc.view;

import com.midrar.fx.mvc.controller.FXController;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.midrar.fx.mvc.utils.Asserts.assertAnnotation;

class AnnotationsParser {
    private Class<?> clazz;

    AnnotationsParser(Class<?> controllerClass) {
        assertAnnotation(controllerClass, FXController.class);
        this.clazz = controllerClass;
    }

    /**
     * Parse the view file url from the @{@link FXController} annotation.
     * @return view file url.
     */
    public URL fxmlFileUrl() {
        FXController fxAnnotation = clazz.getAnnotation(FXController.class);
        String fxmlFileName = fxAnnotation.view();
        // try to load the view file.
        URL fxmlUrl = clazz.getResource(fxmlFileName);
        if (fxmlUrl == null) {
            throw new RuntimeException("Can't find view file: " + fxmlFileName + " defined by @FXController on: " + clazz);
        }
        return fxmlUrl;
    }

    /**
     * Check if the controller is defined in the .view file using 'fx:controller' attribute.
     * @return true if a controller is defined in the .view file or false otherwise.
     */
    public boolean isControllerInFxml() {
        FXController fxAnnotation = clazz.getAnnotation(FXController.class);
        return fxAnnotation.isControllerInFxml();
    }

    /**
     * Get the resource bundle base name if exist, from the @{@link I18n} annotation
     * and return a resource bundle using the default local.
     * @return Optional<ResourceBundle>
     */
    public Optional<ResourceBundle> resourceBundle() {
        I18n i18nAnnotation = clazz.getAnnotation(I18n.class);
        if (i18nAnnotation == null) {
            return Optional.empty();
        }
        String resourceBaseName = i18nAnnotation.value();
        try {
            return Optional.of(ResourceBundle.getBundle(resourceBaseName));
        } catch (Exception e) {
            throw new RuntimeException("Can't load resource bundle: " + resourceBaseName + " defined by: " + clazz, e);
        }
    }

    /**
     * Get the title defined by @{@link Decoration} annotation.
     * @return the title defined by @{@link Decoration} if any
     */
    public String title(Optional<ResourceBundle> bundle) {
        Decoration decorationAnnotation = clazz.getAnnotation(Decoration.class);
        if (decorationAnnotation == null) {
            return "";
        }
        String title = decorationAnnotation.title();
        if(Decoration.LOCALISED_TITLE.equals(title) && bundle.isPresent()){
            if(bundle.get().containsKey(Decoration.LOCALISED_TITLE)){
                return resourceBundle().get().getString(Decoration.LOCALISED_TITLE);
            }
            return "";
        }
        return title;
    }

    /**
     * Get the icons defined by @{@link Decoration} annotation.
     * @return list of icons defined by @{@link Decoration} or {@link Collections.EmptyList} if non.
     */
    public List<Image> icons() {
        Decoration decorationAnnotation = clazz.getAnnotation(Decoration.class);
        if (decorationAnnotation == null) {
            return Collections.EMPTY_LIST;
        }
        String[] icons = decorationAnnotation.icons();
        return Arrays.asList(icons).stream()
                .map(icon -> {
                    URL iconFileUrl = clazz.getResource(icon);
                    if (!icon.isEmpty() && iconFileUrl == null) {
                        throw new RuntimeException("Can't load the icon file: " + icon + " defined by: " + clazz);
                    }
                    return iconFileUrl;
                })
                .map(url -> new Image(url.toExternalForm()))
                .collect(Collectors.toList());
    }

    /**
     * Get CSS files URLs defined by @{@link CSS} annotation.
     */
    public List<String> cssUrls() {
        CSS cssAnnotation = clazz.getAnnotation(CSS.class);
        if (cssAnnotation == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(cssAnnotation.value()).stream()
                .map(cssFileName -> {
                    URL cssFileUrl = clazz.getResource(cssFileName);
                    if (cssFileUrl == null) {
                        throw new RuntimeException("Can't load the CSS file: " + cssFileName + " defined by: " + clazz);
                    }
                    return cssFileUrl;
                })
                .map(cssUrl -> cssUrl.toExternalForm())
                .collect(Collectors.toList());
    }

    /**
     * Create a {@link StageConfigurer} object using options defined by @{@link Stage} annotation.
     * @return StageConfigurer object.
     */
    public Optional<StageConfigurer> stageConfigurer() {
        Stage stageAnnotation = clazz.getAnnotation(Stage.class);
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

}
