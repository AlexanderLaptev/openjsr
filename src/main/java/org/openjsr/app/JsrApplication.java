package org.openjsr.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class JsrApplication extends Application {
    private static final String TITLE = "OpenJSR v1.3.0";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var loader = new FXMLLoader(getClass().getResource("/gui/fxml/main_window.fxml"));
        VBox root = loader.load();
        //noinspection DataFlowIssue
        root.getStylesheets().add(getClass().getResource("/gui/styles/main_window.css").toExternalForm());
        Scene scene = new Scene(root);

        OldMainWindowController controller = loader.getController();

//        scene.setOnKeyPressed(controller::handleCameraMove);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}
