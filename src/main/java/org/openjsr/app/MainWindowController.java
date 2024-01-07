package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
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
import org.openjsr.render.edge.DefaultEdgeRenderStrategy;
import org.openjsr.render.edge.EdgeRenderStrategy;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.framebuffer.PixelFrameBuffer;
import org.openjsr.render.lighting.*;
import org.openjsr.render.shader.LightingShader;
import org.openjsr.render.shader.Shader;
import org.openjsr.render.shader.TextureShader;
import org.openjsr.render.shader.UniformColorShader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainWindowController {

    @FXML
    private ImageView imageView;

    @FXML
    private VBox propertiesPane;

    @FXML
    private TitledPane modelPane;

    @FXML
    private TitledPane cameraPane;

    @FXML
    private TitledPane lightPane;

    @FXML
    private VBox root;

    private Framebuffer framebuffer;

    private Scene scene;

    private SceneRenderer sceneRenderer;

    private PerspectiveCamera activeCamera;

    private Model activeModel;

    private LightingModel lightingModel;

    private EdgeRenderStrategy edgeRenderStrategy;

    private final FileChooser fileChooser = new FileChooser();
    private List<VectorTextField> transformVectorsList;

    private class ModelMenu extends HBox {

        public ModelMenu(int objectId) {

            Button activeButton = new Button("Модель: " + (objectId + 1));
            activeButton.setOnAction(e -> setActiveModel(scene.getModels().get(objectId)));

            Button removeButton = new Button("Удалить");
            removeButton.setOnAction(e -> {
                scene.getModels().remove(objectId);
                setActiveModel(null);
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
        edgeRenderStrategy = new DefaultEdgeRenderStrategy();
        sceneRenderer = new SceneRenderer();
        createView();
        onCreateNewScene();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void createView() {
        PixelFrameBuffer pixelFrameBuffer = new PixelFrameBuffer(1600, 900);
        WritableImage image = new WritableImage(pixelFrameBuffer.getPixelBuffer());
        imageView.setImage(image);
        framebuffer = pixelFrameBuffer;
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
            scene.getModels().add(model);
            setActiveModel(model);
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
        createView();
        updateRightMenu();
        render();
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

        propertiesPane.getChildren().clear();
        if (activeModel != null) {
            VectorTextField scale = new VectorTextField(
                    "Масштаб",
                    activeModel.getTransform().scale.x,
                    activeModel.getTransform().scale.y,
                    activeModel.getTransform().scale.z
                    );
            scale.setOnTyped(e -> setNewTransform());
            VectorTextField rotation = new VectorTextField(
                    "Вращение",
                    activeModel.getTransform().rotation.x,
                    activeModel.getTransform().rotation.y,
                    activeModel.getTransform().rotation.z
            );
            rotation.setOnTyped(e -> setNewTransform());
            VectorTextField position = new VectorTextField(
                    "Позиция",
                    activeModel.getTransform().position.x,
                    activeModel.getTransform().position.y,
                    activeModel.getTransform().position.z
            );
            position.setOnTyped(e -> setNewTransform());
            transformVectorsList = new ArrayList<>();
            transformVectorsList.add(scale);
            transformVectorsList.add(rotation);
            transformVectorsList.add(position);
            propertiesPane.getChildren().addAll(transformVectorsList);
        }
    }

    private void setActiveModel(Model model) {
        activeModel = model;
        updateRightMenu();
    }

    private void setActiveCamera(PerspectiveCamera camera) {
        activeCamera = camera;
        render();
    }

    private void render() {
        if (scene != null) {
            framebuffer.clear();
            sceneRenderer.drawScene(scene, activeCamera, edgeRenderStrategy, framebuffer);
            framebuffer.update();
        }
    }

    @FXML
    public void setNewTransform() {

        Vector3f scale = transformVectorsList.get(0).getVector();
        Vector3f rotation = transformVectorsList.get(1).getVector();
        Vector3f position = transformVectorsList.get(2).getVector();

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
            case I -> {
                activeCamera.rotateUp();
            }
            case K -> {
                activeCamera.rotateDown();
            }
            case L -> {
                activeCamera.rotateRight();
            }
            case J -> {
                activeCamera.rotateLeft();
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
