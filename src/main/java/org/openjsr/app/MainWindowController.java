package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.openjsr.mesh.MeshEditor;
import org.openjsr.render.SceneRenderer;
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
import org.openjsr.render.edge.EdgeRenderStrategy;
import org.openjsr.render.edge.PolygonEdgeRenderStrategy;
import org.openjsr.render.edge.TriangleEdgeRenderStrategy;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.framebuffer.PixelFrameBuffer;
import org.openjsr.render.lighting.DirectionalLightingModel;
import org.openjsr.render.lighting.FlatDirectionalLightingModel;
import org.openjsr.render.lighting.LightingModel;
import org.openjsr.render.lighting.SmoothDirectionalLightingModel;
import org.openjsr.render.shader.LightingShader;
import org.openjsr.render.shader.Shader;
import org.openjsr.render.shader.TextureShader;
import org.openjsr.render.shader.UniformColorShader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainWindowController {

    /**
     * Основной элемент окна.
     */
    @FXML
    private VBox root;

    /**
     * Отображение картинки
     */
    @FXML
    private ImageView imageView;

    /**
     * Часто правой панели, где отображаются камеры
     */
    @FXML
    private VBox cameraBox;

    /**
     * Нижняя часть правой панели со свойствами активной модели
     */
    @FXML
    private VBox propertiesPane;

    /**
     * Часть правой панели, где отображается список моделей.
     */
    @FXML
    private TitledPane modelPane;

    /**
     * Часть правой панели, где отображается список моделей освещения
     */
    @FXML
    private VBox lightningModelsBox;

    /**
     * Часть правой панели, где отображается выбор рендера ребер
     */
    @FXML
    private HBox edgeRenderBox;

    /**
     * Флажок включения рендера ребер
     */
    @FXML
    private CheckBox edgeEnableCheckBox;

    /**
     * Хранилище моделей освещения
     */
    private LightStorage lightStorage;

    private static final Triangulator TRIANGULATOR = SimpleTriangulator.getInstance();

    /**
     * Интерфейс, отвечающий за отрисовку пикселей
     */
    private Framebuffer framebuffer;

    /**
     * Основной экземпляр сцены, где хранятся все модели
     */
    private Scene scene;

    /**
     * Отвечает за рендер сцены
     */
    private SceneRenderer sceneRenderer;

    /**
     * Активная камера, с точки зрения которой идет рендер
     */
    private PerspectiveCamera activeCamera;

    /**
     * Активная модель, информация о которой выводится в нижней части правой панели
     */
    private Model activeModel;

    /**
     * Объект, отвечающий за изменение сетки (удаление вершин, полигонов)
     */
    private MeshEditor meshEditor;

    /**
     * Активная модель освещения, применяемая во время рендера
     */
    private LightingModel activelightingModel;

    /**
     * Отвечает за отрисовку ребер полигонов
     */
    private EdgeRenderStrategy edgeRenderStrategy;

    /**
     * Отвечает за поиск файлов.
     */
    private final FileChooser FILECHOOSER = new FileChooser();

    /**
     * Список VectorTextField в поле свойств модели, отвечает за показ информации о трансформации модели
     */
    private List<VectorTextField> transformVectorsList;

    /**
     * Встроенный класс - элемент списка моделей в правом меню
     */
    private class ModelMenu extends HBox {

        public ModelMenu(int objectId) {

            Button activeButton = new Button("Модель: " + (objectId + 1));
            activeButton.setOnAction(e -> setActiveModel(scene.getModels().get(objectId)));

            Button removeButton = new Button("Удалить");
            removeButton.setOnAction(e -> {
                scene.getModels().remove(objectId);
                setActiveModel(null);
                render();
                updateModelPane();
            });

            Button textureButton = new Button("Добавить текстуру");
            textureButton.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                        "Images",
                        "*.jpeg",
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
                    updateModelPane();
                }
            });

            Button colorButton = new Button("Удалить текстуру");
            colorButton.setOnAction(e -> {
                LightingShader shader = (LightingShader) scene.getModels().get(objectId).getShader();
                if (!(shader.getBaseColorShader() instanceof UniformColorShader)) {
                    shader.setBaseColorShader(new UniformColorShader());
                    render();
                    updateModelPane();
                }
            });
            getChildren().addAll(activeButton, removeButton, textureButton);

            if (((LightingShader) scene.getModels().get(objectId).getShader()).getBaseColorShader() instanceof TextureShader) {
                getChildren().add(colorButton);
            }
        }
    }

    /**
     * Встроенный класс - элемент списка камер в правом меню
     */
    private class CameraMenu extends VBox {
        VectorTextField position;
        VectorTextField target;

        public CameraMenu(int objectId) {


            Button activeButton = new Button("Камера: " + (objectId + 1));
            activeButton.setOnAction(e -> setActiveCamera(scene.getCameras().get(objectId)));

            Button removeButton = new Button("Удалить");
            removeButton.setOnAction(e -> {
                if (scene.getCameras().size() > 1) {
                    scene.getCameras().remove(objectId);
                }
                setActiveCamera(scene.getCameras().get(0));
                updateCameraPane();
                render();
            });
            HBox horizontal1 = new HBox(activeButton, removeButton);

            position = new VectorTextField(
                    "Позиция",
                    scene.getCameras().get(objectId).getPosition().x,
                    scene.getCameras().get(objectId).getPosition().y,
                    scene.getCameras().get(objectId).getPosition().z
            );
            target = new VectorTextField(
                    "Точка направления",
                    scene.getCameras().get(objectId).getViewTarget().x,
                    scene.getCameras().get(objectId).getViewTarget().y,
                    scene.getCameras().get(objectId).getViewTarget().z
            );

            position.setOnTyped(e -> {
                scene.getCameras().get(objectId).setPosition(position.getVector());
                render();
            });

            target.setOnTyped(e -> {
                scene.getCameras().get(objectId).setViewTarget(target.getVector());
                render();
            });

            HBox horizontal2 = new HBox(position, target);

            getChildren().addAll(horizontal1, horizontal2);
        }
    }

    /**
     * Встроенный класс - элемент списка моделей освещения в правом меню
     */
    private class LightningPane extends VBox {
        VectorTextField direction;

        public LightningPane(int objectId) {
            Button activeButton = new Button("Модель освещения: " + (objectId + 1));
            activeButton.setOnAction(e -> setActiveLightingModel(lightStorage.models.get(objectId)));

            Button removeButton = new Button("Удалить");
            removeButton.setOnAction(e -> {
                if (lightStorage.models.size() > 1) {
                    lightStorage.models.remove(objectId);
                }
                setActiveLightingModel(lightStorage.models.get(0));
                updateLightPane();
                render();
            });
            HBox hBox = new HBox(activeButton, removeButton);
            getChildren().add(hBox);

            if (lightStorage.models.get(objectId) instanceof FlatDirectionalLightingModel ||
                    lightStorage.models.get(objectId) instanceof SmoothDirectionalLightingModel) {
                DirectionalLightingModel model = (DirectionalLightingModel) lightStorage.models.get(objectId);
                direction = new VectorTextField(
                        "Направление",
                        model.direction.x,
                        model.direction.y,
                        model.direction.z
                );
                direction.setOnTyped(e -> setDirection(model));
                getChildren().add(direction);
            }
        }

        private void setDirection(DirectionalLightingModel model) {
            model.direction = direction.getVector().cpy().nor();
        }
    }


    /**
     * Метод, инициализирующий основные поля контроллера.
     */
    @FXML
    public void initialize() {
        FILECHOOSER.getExtensionFilters().add(new FileChooser.ExtensionFilter("Трехмерные объекты", "*.obj"));
        FILECHOOSER.setTitle("Выберите файл");
        sceneRenderer = new SceneRenderer();
        meshEditor = new MeshEditor();
        createView();
        createNewScene();
    }

    /**
     * Показывает сообщение с информацией.
     * @param message текст сообщения.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UNDECORATED); // Устанавливаем стиль без декораций
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheets/alert.css")).toExternalForm());
        alert.showAndWait();
    }

    /**
     * Создает FrameBuffer и подгоняет под изображение на экране.
     */
    private void createView() {
        PixelFrameBuffer pixelFrameBuffer = new PixelFrameBuffer(1920, 1280);
        WritableImage image = new WritableImage(pixelFrameBuffer.getPixelBuffer());
        imageView.setImage(image);
        framebuffer = pixelFrameBuffer;
    }

    /**
     * Открывает файл (.obj) и добавляет модель из него в scene.
     */
    @FXML
    private void openFile() {
        Stage stage = (Stage) root.getScene().getWindow();
        File selectedFile = FILECHOOSER.showOpenDialog(stage);
        if (selectedFile != null) {
            MeshReader reader = new ObjReader();
            Mesh mesh;
            try {
                mesh = reader.read(selectedFile);
            } catch (IOException | ObjReaderException e) {
                showAlert(e.getMessage());
                return;
            }
            if (scene == null) {
                createNewScene();
            }
            List<Face> triangles = TRIANGULATOR.triangulateFaces(mesh.faces);
            TriangulatedMesh triangulatedMesh = new TriangulatedMesh(mesh, triangles);
            Shader baseShader = new UniformColorShader();
            Model model = new Model(triangulatedMesh, new LightingShader(baseShader, activelightingModel));
            scene.getModels().add(model);
            setActiveModel(model);
            render();
        }
    }

    /**
     * Сохраняет активную модель в файл.
     */
    @FXML
    private void saveFile() {
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

    /**
     * Создает базовую модель освещения.
     */
    private void createLightStorage() {
        lightStorage = new LightStorage();
        activelightingModel = new SmoothDirectionalLightingModel();
        ((DirectionalLightingModel) activelightingModel).direction = activeCamera.getPosition().cpy().scl(-1).nor();
        lightStorage.models.add(activelightingModel);
        updateLightPane();
    }

    /**
     * Создает новую сцену (обнуляет старую).
     */
    @FXML
    private void createNewScene() {
        scene = new Scene();
        addCamera();
        createView();
        createLightStorage();
        updateRightMenu();
        render();
    }

    /**
     * Устанавливает активную модель. Эта модель будет отображаться снизу в правой панели и доступна для сохранения.
     * @param model
     */
    private void setActiveModel(Model model) {
        activeModel = model;
        updateModelPane();
        updatePropertiesPane();
    }

    /**
     * Устанавливает активную камеру. С точки зрения этой камеры будет происходить рендер.
     * @param camera
     */
    private void setActiveCamera(PerspectiveCamera camera) {
        activeCamera = camera;
        render();
    }

    /**
     * Устанавливает активную модель освещения. С такой моделью освещения будет отрисовываться сцена.
     * @param model
     */
    private void setActiveLightingModel(LightingModel model) {
        activelightingModel = model;
        if (scene == null) createNewScene();
        for (Model m : scene.getModels()) {
            LightingShader shader = (LightingShader) m.getShader();
            shader.setLightingModel(model);
            render();
        }
    }

    /**
     * Перерисовывает сцену с помощью frameBuffer.
     */
    private void render() {
        if (scene != null) {
            sceneRenderer.drawScene(scene, activeCamera, edgeRenderStrategy, framebuffer);
            framebuffer.update();
        }
    }

    /**
     * Изменяет трансформацию активной модели.
     */
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

    /**
     * Изменяет перемещение камеры с помощью нажатий клавиш.
     * @param event обрабатываются только:
     *              W - движение вперед
     *              A - движение влево
     *              S - движение вправо
     *              D - движение назад
     *              Q - движение вниз
     *              E - движение вверх
     *              I - поворот наверх
     *              K - поворот вниз
     *              L - поворот направо
     *              J - поворот налево.
     */
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
        updateCameraPane();
    }

    /**
     * Добавляет базовую камеру в список камер. Переключает на нее активность.
     */
    @FXML
    private void addCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(new Vector3f(5, 1.5f, 0));
        if (scene == null) {
            createNewScene();
            return;
        }
        scene.getCameras().add(camera);
        setActiveCamera(camera);
        updateCameraPane();
    }

    /**
     * Включает edgeRenderStrategy
     * @param actionEvent
     */
    @FXML
    private void enableEdgeRender(ActionEvent actionEvent) {
        if (edgeEnableCheckBox.isSelected()) {
            edgeRenderStrategy = new TriangleEdgeRenderStrategy();
        } else {
            edgeRenderStrategy = null;
        }
        updateEdgeRenderPane();
        render();
    }

    /**
     * Добавляет модель освещения. Вызывает диалоговое окно из LightStorage.
     */
    @FXML
    private void addLightingModel() {
        LightStorage.LightTypes input = lightStorage.addLightningModelDialog();
        lightStorage.chooseModel(input);
        updateLightPane();
    }

    /**
     * Обновляет все элементы правого меню.
     */
    private void updateRightMenu() {
        if (scene == null) {
            return;
        }
        updateModelPane();
        updateCameraPane();
        updateLightPane();
        updateEdgeRenderPane();
        updatePropertiesPane();
    }

    /**
     * Обновляет свойства активной модели панели снизу.
     */
    private void updatePropertiesPane() {
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

            Label deletionVertexLabel = new Label("Удаление вершин");
            HBox deletionVertexBox = new HBox();
            Label textLabel1 = new Label("Id вершины:");
            TextField verIndexText = new TextField();
            Button deleteButton = new Button("Удалить");
            deleteButton.setOnAction(e -> {
                int vertexIndex;
                try {
                    vertexIndex = Integer.parseInt(verIndexText.getText());
                } catch (Exception ignored) {
                    showAlert("Неверно указана вершина");
                    verIndexText.setText("");
                    return;
                }
                try {
                    meshEditor.removeVertex(activeModel.getMesh(), vertexIndex);
                } catch (MeshEditor.MeshEditorException exc) {
                    showAlert(exc.getMessage());
                }
                verIndexText.setText("");
                render();
            });
            deletionVertexBox.getChildren().addAll(textLabel1, verIndexText, deleteButton);
            deletionVertexBox.setAlignment(Pos.CENTER_RIGHT);
            propertiesPane.getChildren().addAll(deletionVertexLabel, deletionVertexBox);

            Label deletionFaceLabel = new Label("Удаление полигона");
            HBox deletionFaceBox = new HBox();
            Label textLabel2 = new Label("Id полигона:");
            TextField faceIndexText = new TextField();
            Button deleteFaceButton = new Button("Удалить");
            deleteFaceButton.setOnAction(e -> {
                int vertexIndex;
                try {
                    vertexIndex = Integer.parseInt(faceIndexText.getText());
                } catch (Exception ignored) {
                    showAlert("Неверно указан полигон");
                    faceIndexText.setText("");
                    return;
                }
                try {
                    meshEditor.removeFace(activeModel.getMesh(), vertexIndex);
                } catch (MeshEditor.MeshEditorException exc) {
                    showAlert(exc.getMessage());
                }
                faceIndexText.setText("");
                render();
            });
            deletionFaceBox.getChildren().addAll(textLabel2, faceIndexText, deleteFaceButton);
            deletionFaceBox.setAlignment(Pos.CENTER_RIGHT);
            propertiesPane.getChildren().addAll(deletionFaceLabel, deletionFaceBox);
        }
    }

    /**
     * Обновляет список моделей на панели.
     */
    private void updateModelPane() {
        modelPane.setContent(null);
        if (scene == null) return;

        List<Model> models = scene.getModels();
        if (models.isEmpty()) return;

        VBox modelLayout = new VBox();
        for (int modelInd = 0; modelInd < models.size(); modelInd++) {
            ModelMenu modelMenu = new ModelMenu(modelInd);
            modelLayout.getChildren().add(modelMenu);
        }
        modelPane.setContent(modelLayout);
    }

    /**
     * Обновляет список камер на панели.
     */
    private void updateCameraPane() {
        cameraBox.getChildren().clear();
        List<PerspectiveCamera> cameras = scene.getCameras();
        for (int cameraInd = 0; cameraInd < cameras.size(); cameraInd++) {
            CameraMenu cameraMenu = new CameraMenu(cameraInd);
            cameraBox.getChildren().add(cameraMenu);
        }
    }

    /**
     * Обновляет список моделей освещения на панели.
     */
    private void updateLightPane() {
        lightningModelsBox.getChildren().clear();
        for (int lightModelInt = 0; lightModelInt < lightStorage.models.size(); lightModelInt++) {
            lightningModelsBox.getChildren().add(new LightningPane(lightModelInt));
        }
    }

    /**
     * Обновляет панель с выбором отрисовки ребер
     */
    private void updateEdgeRenderPane() {
        edgeRenderBox.getChildren().clear();
        if (edgeEnableCheckBox.isSelected()) {
            Button triangleButton = new Button("Отрисовать грани треугольников");
            triangleButton.setOnAction(e -> {
                edgeRenderStrategy = new TriangleEdgeRenderStrategy();
                render();
            });

            Button polygonButton = new Button("Отрисовать грани полигонов");
            polygonButton.setOnAction(e -> {
                edgeRenderStrategy = new PolygonEdgeRenderStrategy();
                render();
            });
            edgeRenderBox.getChildren().addAll(triangleButton, polygonButton);
        }
    }
}
