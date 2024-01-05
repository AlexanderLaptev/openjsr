package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.openjsr.core.Transform;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;
import org.openjsr.mesh.reader.MeshReader;
import org.openjsr.mesh.reader.ObjReader;
import org.openjsr.mesh.reader.ObjReaderException;
import org.openjsr.mesh.triangulation.SimpleTriangulator;
import org.openjsr.mesh.triangulation.TriangulatedMesh;
import org.openjsr.mesh.triangulation.Triangulator;
import org.openjsr.render.Model;
import org.openjsr.render.Scene;
import org.openjsr.render.framebuffer.CanvasFramebuffer;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.lighting.FlatLightingModel;
import org.openjsr.render.lighting.FullbrightLightingModel;
import org.openjsr.render.shader.UniformColorShader;
import org.openjsr.core.PerspectiveCamera;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainWindowController {
    @FXML
    private Canvas canvas;
    @FXML
    private VBox root;
    private Framebuffer framebuffer;
    private Scene scene;
    private PerspectiveCamera camera;

    @FXML
    public void initialize() {
        canvas.setWidth(1600);
        canvas.setHeight(900);
        onCanvasResized();
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
    private void onCanvasResized() {
        framebuffer = new CanvasFramebuffer(canvas);
    }

    @FXML
    private void onOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        Stage stage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            MeshReader reader = new ObjReader();
            Mesh mesh = new Mesh();
            try {
                mesh = reader.read(selectedFile);
            } catch (IOException | ObjReaderException e) {
                showAlert(e.getMessage());
            }
            if (scene == null) {
                onCreateNewScene();
            }
                Triangulator triangulator = new SimpleTriangulator();
                List<Face> triangles = triangulator.triangulateFaces(mesh.faces);
                TriangulatedMesh triangulatedMesh = new TriangulatedMesh(mesh, triangles);
                Transform tr = new Transform();
                tr.scale.set(3);
                tr.recalculateMatrices();
                Model model = new Model(triangulatedMesh, tr, new UniformColorShader());
                scene.getModels().clear();
                scene.getModels().add(model);
                framebuffer.clear();
                scene.render(camera, new FullbrightLightingModel(), framebuffer);
        }
    }

    @FXML
    private void onCreateNewScene() {
        scene = new Scene();
        camera = new PerspectiveCamera();
        camera.setPosition(new Vector3f(10, 10, 6));
        camera.setViewTarget(new Vector3f(0, 0, 0));
        framebuffer = new CanvasFramebuffer(canvas);
    }

    @FXML
    private void onMouseClicked(MouseEvent mouseEvent) {
        System.out.println("clicked");
    }
}
