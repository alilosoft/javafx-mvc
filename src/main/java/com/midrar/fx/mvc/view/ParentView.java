/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.midrar.fx.mvc.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.*;

import java.net.URL;
import java.util.*;

import static com.midrar.fx.mvc.utils.Asserts.assertParameterNotNull;

/**
 */
@Data
@Setter(AccessLevel.NONE)
abstract class ParentView<T> implements View<T>{
    private T controller;
    @NonNull private Parent rootNode;
    private String title = "";
    private List<Image> icons = Collections.EMPTY_LIST;
    private List<String> cssUrls = Collections.EMPTY_LIST;
    private ViewContext viewContext;
    protected AnnotationsParser annotationsParser;

    ParentView(T _controller) {
        assertParameterNotNull(_controller, "controller");
        controller = _controller;
        annotationsParser = new AnnotationsParser(controller.getClass());
        rootNode = loadRoot(controller);
        title = annotationsParser.title();
        icons = annotationsParser.icons();
        cssUrls = annotationsParser.cssUrls();
    }

    /**
     * Load the root node hierarchy of this {@link ParentView}.
     * @param controller
     * @return a {@link Parent} Node represented by this {@link ParentView}.
     */
    private Parent loadRoot(Object controller) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        if (annotationsParser.isControllerInFxml()) {
            // if the controller class is defined in the .view file using 'fx:controller'
            // then make sure that the controller factory used by fxmlLoader will return
            // the same controller instance of the ParentView  being initialized.
            fxmlLoader.setControllerFactory(c -> controller);
        } else {
            // if the controller class is not defined in the .view file then pass the controller instance
            // of the ParentView being initialized to fxmlLoader.
            fxmlLoader.setController(controller);
        }
        // parse the .view file url defined by @FXController
        URL fxmlUrl = annotationsParser.fxmlFileUrl();
        fxmlLoader.setLocation(fxmlUrl);
        // parse and set the resource bundle if defined by @FXController
        Optional<ResourceBundle> resourceBundle = annotationsParser.resourceBundle();
        resourceBundle.ifPresent(fxmlLoader::setResources);
        try {
            return fxmlLoader.load();
        } catch (Exception e) {
            throw new RuntimeException("Unable to load node hierarchy from: " + fxmlUrl, e);
        }
    }

    void setViewContext(ViewContext _viewContext) {
        viewContext = _viewContext;
        viewContext.registerView(controller.getClass(),this);
    }

    //TODO: move to concrete class PaneView that extends view
    public void addToPane(Pane pane) {
        assertParameterNotNull(pane, "pane");
        pane.getChildren().add(rootNode);
    }

    //TODO:
    public void addToTabPane(TabPane tabPane) {
        assertParameterNotNull(tabPane, "tabPan");
        Tab tab = new Tab(title, rootNode);
        Optional<Image> icon = icons.stream()// find the smallest icon from the icons list.
                .reduce((icon1, icon2) -> icon1.getHeight() < icon2.getHeight() ? icon1 : icon2);

        if (icon.isPresent()) {
            tab.setGraphic(new ImageView(icon.get()));
        }
        // or: using lambda
        //icon.ifPresent(i -> tab.setGraphic(new ImageView(i)));
        tabPane.getStylesheets().addAll(cssUrls);
        tabPane.getTabs().add(tab);
    }

}