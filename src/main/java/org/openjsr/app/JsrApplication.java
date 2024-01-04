package org.openjsr.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class JsrApplication extends Application {
    private static final String TITLE = "OpenJSR v0.1.0-alpha";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var loader = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"));
        VBox root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.show();
        primaryStage.centerOnScreen();

    }
}
