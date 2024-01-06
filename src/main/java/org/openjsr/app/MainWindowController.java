package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
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
import org.openjsr.mesh.writer.MeshWriter;
import org.openjsr.mesh.writer.ObjWriter;
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
    private TextField positionX;

    @FXML
    private TextField positionY;

    @FXML
    private TextField positionZ;

    @FXML
    private TextField rotationX;

    @FXML
    private TextField rotationY;

    @FXML
    private TextField rotationZ;

    @FXML
    private TextField scaleX;

    @FXML
    private TextField scaleY;

    @FXML
    private TextField scaleZ;

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

        public ModelMenu(int objectId) {
            this.objectId = objectId;

            Button activeButton = new Button("Модель: " + (objectId + 1));
            activeButton.setOnAction(e -> setActiveModel(scene.getModels().get(objectId)));

            Button visibleCheckBox = new Button("Удалить");
            visibleCheckBox.setOnAction(e -> {
                scene.getModels().remove(objectId);
                render();
                updateRightMenu();
            });

            getChildren().addAll(activeButton, visibleCheckBox);
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Трехмерные объекты", "*.obj"));
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
            scene.getModels().add(model);
            render();
        }
    }

    @FXML
    private void onSaveFile() {
        if (activeModel == null) {
            showAlert("Не выбрана активная модель");
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Трехмерные объекты", "*.obj"));
        Stage stage = (Stage) root.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            MeshWriter writer = new ObjWriter();
            try {
                writer.write(activeModel.getMesh(), file);
            } catch (ObjReaderException e) {
                showAlert(e.getMessage());
            }
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
        Transform transform = model.getTransform();
        positionX.setText(String.valueOf(transform.position.x));
        positionY.setText(String.valueOf(transform.position.y));
        positionZ.setText(String.valueOf(transform.position.z));

        scaleX.setText(String.valueOf(transform.scale.x));
        scaleY.setText(String.valueOf(transform.scale.y));
        scaleZ.setText(String.valueOf(transform.scale.z));

        rotationX.setText(String.valueOf(transform.rotation.x));
        rotationY.setText(String.valueOf(transform.rotation.y));
        rotationZ.setText(String.valueOf(transform.rotation.z));
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

    @FXML
    private void setNewTransform() {
        //todo: проверка на плохие строчки
        Vector3f position = new Vector3f(
                Float.parseFloat(positionX.getText()),
                Float.parseFloat(positionY.getText()),
                Float.parseFloat(positionZ.getText())
        );

        Vector3f scale = new Vector3f(
                Float.parseFloat(scaleX.getText()),
                Float.parseFloat(scaleY.getText()),
                Float.parseFloat(scaleZ.getText())
        );

        Vector3f rotation = new Vector3f(
                Float.parseFloat(rotationX.getText()),
                Float.parseFloat(rotationY.getText()),
                Float.parseFloat(rotationZ.getText())
        );

        activeModel.setTransform(new Transform(
                position,
                rotation,
                scale
        ));
        activeModel.getTransform().recalculateMatrices();
        render();
    }
}
