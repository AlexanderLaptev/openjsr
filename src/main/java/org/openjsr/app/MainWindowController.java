package org.openjsr.app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainWindowController {

    @FXML
    public void initialize() {
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
