package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
import org.openjsr.core.PerspectiveCamera;
import org.openjsr.render.shader.UniformColorShader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainWindowController {
    @FXML
    private TitledPane modelPane;

    @FXML
    private TitledPane cameraPane;

    @FXML
    private TitledPane lightPane;

    @FXML
    private Canvas canvas;

    @FXML
    private VBox root;

    private Framebuffer framebuffer;

    private Scene scene;

    private PerspectiveCamera activeCamera;

    private List<PerspectiveCamera> cameras;

    private Model activeModel;

    private class ModelMenu extends HBox {
        private int objectId;
        private Button activeButton;
        private Button visibleCheckBox;

        public ModelMenu(int objectId) {
            this.objectId = objectId;

            activeButton = new Button("Модель: " + (objectId + 1));
            activeButton.setOnAction(e -> {
                setActiveModel(scene.getModels().get(objectId));
            });

            visibleCheckBox = new Button("Удалить");
            visibleCheckBox.setOnAction(e -> {
                scene.getModels().remove(objectId);
                render();
                updateRightMenu();
            });

            getChildren().addAll(activeButton, visibleCheckBox);
        }

        public int getObjectId() {
            return objectId;
        }

        public void setObjectId(int objectId) {
            this.objectId = objectId;
        }
    }


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
            setActiveModel(model);
            //scene.getModels().clear();
            scene.getModels().add(model);
            render();
        }
    }

    @FXML
    private void onCreateNewScene() {
        scene = new Scene();
        activeCamera = new PerspectiveCamera();
        activeCamera.setPosition(new Vector3f(10, 10, 6));
        activeCamera.setViewTarget(new Vector3f(0, 0, 0));
        framebuffer = new CanvasFramebuffer(canvas);
    }

    @FXML
    private void onMouseClicked(MouseEvent mouseEvent) {
        System.out.println("clicked");
    }

    private void updateRightMenu() {
        if (scene == null) {
            return;
        }
        List<Model> models = scene.getModels();
        VBox layout = new VBox();
        for (int modelInd = 0; modelInd < models.size(); modelInd++) {
            ModelMenu modelMenu = new ModelMenu(modelInd);
            layout.getChildren().add(modelMenu);
        }
        modelPane.setContent(layout);
    }

    private void setActiveModel(Model model) {
        activeModel = model;
    }

    private void render() {
        if (scene != null) {
            framebuffer.clear();
            updateRightMenu();
            FlatLightingModel lightingModel = new FlatLightingModel();
            lightingModel.lightPosition = new Vector3f(5, 5, 5);
            lightingModel.ambientLight = 0.5f;

            scene.render(activeCamera, lightingModel, framebuffer);
        }
    }
}
