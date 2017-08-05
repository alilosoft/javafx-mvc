package com.midrar.fx.mvc.core;

import java.io.IOException;
import java.net.URL;

public class FXMLLoaderException extends RuntimeException {
    public FXMLLoaderException(URL fxml, Exception cause) {
        super("FXMLLoader fails to load fxml file: "+ fxml, cause);
    }
}
