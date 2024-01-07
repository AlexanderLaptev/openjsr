package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.openjsr.SceneRenderer;
import org.openjsr.core.PerspectiveCamera;
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
import org.openjsr.render.lighting.DirectionalLightingModel;
import org.openjsr.render.lighting.LightingModel;
import org.openjsr.render.lighting.SmoothDirectionalLightingModel;
import org.openjsr.render.shader.LightingShader;
import org.openjsr.render.shader.Shader;
import org.openjsr.render.shader.TextureShader;
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

    private SceneRenderer sceneRenderer;

    private PerspectiveCamera activeCamera;

    private Model activeModel;

    private LightingModel lightingModel;

    private final FileChooser fileChooser = new FileChooser();

    private class ModelMenu extends HBox {

        public ModelMenu(int objectId) {

            Button activeButton = new Button("Модель: " + (objectId + 1));
            activeButton.setOnAction(e -> setActiveModel(scene.getModels().get(objectId)));

            Button removeButton = new Button("Удалить");
            removeButton.setOnAction(e -> {
                scene.getModels().remove(objectId);
                render();
                updateRightMenu();
            });

            Button textureButton = new Button("Добавить текстуру");
            textureButton.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                        "Images",
                        "*.jpg",
                        "*.png",
                        "*.gif"
                ));
                Stage stage = (Stage) root.getScene().getWindow();
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    Image image = new Image(selectedFile.toURI().toString());
                    LightingShader shader = (LightingShader) scene.getModels().get(objectId).getShader();
                    shader.setBaseColorShader(new TextureShader(image));
                    render();
                }

            });

            getChildren().addAll(activeButton, removeButton, textureButton);
        }
    }

    private class CameraMenu extends HBox {

        public CameraMenu(int objectId) {

            Button activeButton = new Button("Камера: " + (objectId + 1));
            activeButton.setOnAction(e -> setActiveCamera(scene.getCameras().get(objectId)));

            Button removeButton = new Button("Удалить");
            removeButton.setOnAction(e -> {
                if (scene.getCameras().size() > 1) {
                    scene.getCameras().remove(objectId);
                }
                setActiveCamera(scene.getCameras().get(0));
                updateRightMenu();
                render();
            });
            getChildren().addAll(activeButton, removeButton);
        }
    }


    @FXML
    public void initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Трехмерные объекты", "*.obj"));
        fileChooser.setTitle("Выберите файл");
        sceneRenderer = new SceneRenderer();
        canvas.setWidth(1600);
        canvas.setHeight(900);
        onCanvasResized();
        onCreateNewScene();
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
            Shader baseShader = new UniformColorShader();
            Model model = new Model(triangulatedMesh, new LightingShader(baseShader, lightingModel));
            setActiveModel(model);
            scene.getModels().add(model);
            updateRightMenu();
            render();
        }
    }

    @FXML
    private void onSaveFile() {
        if (activeModel == null) {
            showAlert("Не выбрана активная модель");
            return;
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
        addCamera();
        lightingModel = new SmoothDirectionalLightingModel();
        DirectionalLightingModel lm = (DirectionalLightingModel) lightingModel;
        lm.direction = activeCamera.getPosition().cpy().scl(-1).nor();
        framebuffer = new CanvasFramebuffer(canvas);
        updateRightMenu();
        render();
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
        VBox modelLayout = new VBox();
        for (int modelInd = 0; modelInd < models.size(); modelInd++) {
            ModelMenu modelMenu = new ModelMenu(modelInd);
            modelLayout.getChildren().add(modelMenu);
        }
        modelPane.setContent(modelLayout);

        VBox camerasLayout = new VBox();
        Button addButton = new Button("Добавить камеру");
        addButton.setOnAction(e -> addCamera());
        camerasLayout.getChildren().add(addButton);
        for (int cameraInd = 0; cameraInd < scene.getCameras().size(); cameraInd++) {
            CameraMenu cameraMenu = new CameraMenu(cameraInd);
            camerasLayout.getChildren().add(cameraMenu);
        }
        cameraPane.setContent(camerasLayout);
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

    private void setActiveCamera(PerspectiveCamera camera) {
        activeCamera = camera;
        render();
    }

    private void render() {
        if (scene != null) {
            framebuffer.clear();
//            scene.render(activeCamera, lightingModel, framebuffer);
            sceneRenderer.drawScene(scene, activeCamera, null, framebuffer);
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

    @FXML
    public void handleCameraMove(KeyEvent event) {
        if (activeCamera == null) {
            return;
        }
        switch (event.getCode()) {
            case W -> {
                activeCamera.moveForward();
            }
            case A -> {
                activeCamera.moveLeft();
            }
            case S -> {
                activeCamera.moveBackward();
            }
            case D -> {
                activeCamera.moveRight();
            }
            case E -> {
                activeCamera.moveUp();
            }
            case Q -> {
                activeCamera.moveDown();
            }
        }
        render();
    }

    @FXML
    private void addCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(new Vector3f(5, 1.5f, 0));
        if (scene == null) {
            onCreateNewScene();
            return;
        }
        scene.getCameras().add(camera);
        setActiveCamera(camera);
        updateRightMenu();
    }
}
