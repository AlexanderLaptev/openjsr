package org.openjsr.app;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import org.openjsr.render.framebuffer.CanvasFramebuffer;
import org.openjsr.render.framebuffer.Framebuffer;

public class MainWindowController {
    private Framebuffer framebuffer;

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

    /**
     * Пересоздаёт framebuffer при изменении размера canvas.
     * @param canvas Canvas, который изменил размер.
     */
    private void onCanvasResized(Canvas canvas) {
        framebuffer = new CanvasFramebuffer(canvas);
    }
}
