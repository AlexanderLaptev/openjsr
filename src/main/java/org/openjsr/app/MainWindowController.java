package org.openjsr.app;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;

public class MainWindowController {

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleSettingsButton1() {
        showAlert("Settings Button 1 Clicked");
    }

    @FXML
    private void handleHelpButton1() {
        showAlert("Help Button 1 Clicked");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
