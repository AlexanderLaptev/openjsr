package org.openjsr.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController {

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleSettingsButton1() {
        showAlert("Settings Button 1 Clicked");
    }

    @FXML
    private void handleSettingsButton2() {
        showAlert("Settings Button 2 Clicked");
    }

    @FXML
    private void handleHelpButton1() {
        showAlert("Help Button 1 Clicked");
    }

    @FXML
    private void handleHelpButton2() {
        showAlert("Help Button 2 Clicked");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
