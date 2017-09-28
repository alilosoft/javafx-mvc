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

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.*;

import java.util.*;

import static com.midrar.fx.mvc.utils.Asserts.assertParameterNotNull;

/**
 */
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@ToString(of = "controller")
public class View<T> {
    private T controller;
    private Parent rootNode;
    private String title = "";
    private List<Image> icons = Collections.EMPTY_LIST;
    private List<String> cssUrls = Collections.EMPTY_LIST;
    private Optional<StageConfigurer> stageConfigurer = Optional.empty();
    private Optional<Stage> stage = Optional.empty();
    private ViewContext viewContext;

    void setViewContext(ViewContext viewContext) {
        this.viewContext = viewContext;
        this.viewContext.registerView(this);
    }

    /**
     * Show this {@link View} in the provided {@link Stage}.
     * >Note: If this method is called directly from client code, and a stage configuration is defined by
     * >@{@link com.midrar.fx.mvc.view.Stage} annotation, then that config will not be applied to the stage by this
     * method.
     */
    public void showInStage(Stage stage) {
        assertParameterNotNull(stage, "stage");
        if (isShowing()) {
            Stage currentStage = (Stage) rootNode.getScene().getWindow();
            if(Objects.equals(stage, currentStage)){
                currentStage.toFront();
                return;
            }else{
                throw new RuntimeException("The "+this+" is already shown in another Stage!");
            }
        }
        this.stage = Optional.of(stage);
        Scene scene = rootNode.getScene();
        if (scene == null) {
            scene = new Scene(rootNode);
            scene.getStylesheets().addAll(cssUrls);
            if (Locale.getDefault().equals(new Locale("ar"))) {
                scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            }
        }
        stage.setScene(scene);
        stage.setTitle(title);
        stage.getIcons().clear();
        stage.getIcons().addAll(icons);
        stage.show();
    }

    /**
     * Show this {@link View} in its own {@link Stage}.
     * >Note: if a stage configuration is provided using the @{@link com.midrar.fx.mvc.view.Stage}
     * annotation then it will be applied for the stage used to show this {@link View} .
     */
    public void show() {
        if(!stage.isPresent()){
            stage = Optional.of(new Stage());
            stageConfigurer.ifPresent(sc -> sc.configure(stage.get()));
        }
        showInStage(stage.get());
    }

    public void addToPane(Pane pane) {
        assertParameterNotNull(pane, "pane");
        pane.getChildren().add(rootNode);
    }

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

    private boolean isShowing(){
        return rootNode.getScene() != null &&
                rootNode.getScene().getWindow() != null &&
                rootNode.getScene().getWindow().isShowing();
    }

    public void close() {
        /**
         * if this value is shown in a stage then close the stage;
         * if it is shown in a TabPane then remove the tab of this value
         * if it is shown in a Pane then-> multiple scenarios ->
         *  1- if the pane is a rootNode of a value (i.e: shown in tab or stage) then simply close the value that value;
         *  2- if the pane is part of a layout then try to remove with an appropriate animation effect.
         */
        stage.ifPresent(s -> s.close());
    }
}
