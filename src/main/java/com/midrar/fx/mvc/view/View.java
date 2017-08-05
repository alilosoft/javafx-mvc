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

import javafx.beans.property.*;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.*;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.midrar.fx.mvc.utils.Asserts.assertParameterNotNull;

/**
 * # View class: is an abstraction of javafx {@link Parent}
 * > A View object is a representation of a {@link Node} hierarchy defined by a .fxml file.
 * It encapsulate the following elements:
 *
 * >- The {@link java.net.URL} of the .fxml file.
 * - The {@link Node} hierarchy loaded from the .fxml file.
 * - A {@link String} title to be used if shown in a {@link javafx.stage.Stage} or a {@link javafx.scene.control.Tab}.
 * - A list of icons as {@link java.util.List}<{@link javafx.scene.image.Image}> to use if shown in a {@link javafx.stage.Stage} or a {@link javafx.scene.control.Tab}.
 * - A {@link java.net.URL} to a .css file to add to the scene used to show the View.
 * - A reference to the controller object associated to this View.
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class View<Controller> {
    @NonNull
    private Class controllerClass;
    private Controller controller;
    private Parent rootNode;

    private URL fxmlFileUrl;
    private ResourceBundle resourceBundle;
    private String title;
    private List<Image> icons;
    private List<String> cssUrls;

    private StageConfigurer stageConfigurer;

    private BooleanProperty isShown = new SimpleBooleanProperty(); ;
    private StringProperty titleProperty = new SimpleStringProperty();

    public void showInStage(Stage stage) {
        if(isShown.get()){
            throw new RuntimeException(this +" is already shown!");
        }
        stage.setOnShown(e-> isShown.setValue(true));
        stage.setOnHidden(e-> isShown.setValue(false));

        Scene scene = rootNode.getScene();
        if (scene == null) {
            scene = new Scene(rootNode);
        }

        if(resourceBundle.getLocale().getLanguage().equals(new Locale("ar").getLanguage())){
            scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }

        if (cssUrls != null){
            scene.getStylesheets().addAll(cssUrls);
        }

        scene.setFill(Color.TRANSPARENT);//TODO: show a transparent stage

        stage.setScene(scene);
        stage.setTitle(title);
        if(icons != null){
            stage.getIcons().addAll(icons);
        }

        if(stageConfigurer != null){
            stage = stageConfigurer.configure(stage);
        }
        stage.show();
    }

    public void showInNewStage() {
        //TODO: bug fix, prevent showing the same view in different stages.
        if(rootNode.getScene() != null){
            //throw new RuntimeException("Same view can't be shown in multiple scenes!");
        }
        showInStage(new Stage());
    }

    /**
     * Show this view in a new {@link Stage} configured by the passed {@link StageConfigurer}.
     * >Note: that the @{@link com.midrar.fx.mvc.view.Stage} if present, will be omitted, and the passed {@link StageConfigurer}
     * will take precedence off it.
     * @param stageConfigurer
     */
    public void showInNewStage(StageConfigurer stageConfigurer) {
        this.stageConfigurer = stageConfigurer;
        showInStage(new Stage());
    }

    public void showInScene(Scene scene){
        assertParameterNotNull(scene, "scene");
        if(cssUrls != null) scene.getStylesheets().addAll(cssUrls);
        scene.setRoot(rootNode);
    }

    public void addToPane(Pane pane) {
        assertParameterNotNull(pane, "pane");
        pane.getChildren().add(rootNode);
    }

    public void addToTabPane(TabPane tabPane) {
        assertParameterNotNull(tabPane, "tabPan");
        Tab tab = new Tab(title, rootNode);
        Optional<Image> icon = icons.stream()// get the smallest icon from the icons list.
                .reduce((icon1, icon2) -> icon1.getHeight() < icon2.getHeight() ? icon1 : icon2);

        if (icon.isPresent()) {
            tab.setGraphic(new ImageView(icon.get()));
        }
        if (cssUrls != null){
            tabPane.getStylesheets().addAll(cssUrls);
        }
        tabPane.getTabs().add(tab);
    }

    public void close() {
        /**
         * if this controllerClass is shown in a stage then close the stage;
         * if it is shown in a TabPane then remove the tab of this controllerClass
         * if it is shown in a Pane then-> multiple scenarios ->
         *  1- if the pane is a rootNode of a controllerClass (i.e: shown in tab or stage) then simply close the controllerClass that controllerClass;
         *  2- if the pane is part of a layout then try to remove with an appropriate animation effect.
         */
    }
}
