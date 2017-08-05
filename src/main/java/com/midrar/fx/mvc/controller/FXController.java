/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers All
 * rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of DataFX, the website
 * javafxdata.org, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.midrar.fx.mvc.controller;

import com.midrar.fx.mvc.view.View;
import com.midrar.fx.mvc.view.ViewLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define a controller class...TODO...doc
  * @author @lilo$oft
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FXController {
    /**
     * Defines the .fxml file that defines the {@link javafx.scene.Node} hierarchy of the {@link View}
     * to be controlled by the annotated controller class.
     */
    String fxml() default "";

    /**
     *Set this flag to false to inform the {@link ViewLoader} that the controller is not defined in the .fxml file.
     * In that case the {@link ViewLoader} will delegate the controller instantiation to the provided {@link ControllerManager},
     * and passes the created instance to te {@link javafx.fxml.FXMLLoader} implicitly.
     *>Note: that if this flag is not set to false, and no controller is defined in .fxml file then the loaded view won't have
     * any controller.
     */
    boolean isDefinedInFxml() default true;

}

