package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjsr.render.lighting.*;

import java.util.*;

import static javafx.collections.FXCollections.*;

/**
 * Класс - хранилище созданных пользователем моделей освещения.
 */
public class LightStorage {

    /**
     * Список всех моделей освещения.
     */
    public List<LightingModel> models = new ArrayList<>();

    public enum LightTypes {
        UNIFORM,
        FLAT,
        SMOOTH,
        NONE
    }

    public LightStorage() {
    }

    /**
     * Вызывает диалоговое окно с предложением выбрать тип освещения.
     *
     * @return номер строки в списке, которую выбрал пользователь.
     */
    public LightTypes addLightningModelDialog() {
        Dialog<Integer> addDialog = new Dialog<>();

        String cssPath = Objects.requireNonNull(getClass().getResource("/stylesheets/dialog-panel.css")).toExternalForm();
        addDialog.getDialogPane().getStylesheets().add(cssPath);

        addDialog.setTitle("Выберите тип модели освещения");

        ButtonType okButtonType = new ButtonType("OК", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        addDialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        VBox dialogContent = new VBox();

        ComboBox<String> typesBox = new ComboBox<>();
        typesBox.setItems(
                observableList(
                        Arrays.asList(
                                "Равномерное освещение",
                                "Плоское освещение",
                                "Гладкое освещение"
                        )
                )
        );

        dialogContent.getChildren().add(typesBox);

        addDialog.getDialogPane().setContent(dialogContent);

        addDialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return typesBox.getSelectionModel().getSelectedIndex();
            }
            return null;
        });

        Optional<Integer> res = addDialog.showAndWait();
        if (res.isPresent()) {
            switch (res.get()) {
                case 0 -> {
                    return LightTypes.UNIFORM;
                }
                case 1 -> {
                    return LightTypes.FLAT;
                }
                case 2 -> {
                    return LightTypes.SMOOTH;
                }
            }
        }
        return LightTypes.NONE;
    }

    /**
     * Выбирает тип модели из доступных.
     *
     * @param input номер модели в списке.
     */
    public void chooseModel(LightTypes input) {

        switch (input) {
            case UNIFORM-> {
                UniformLightingModel model = new UniformLightingModel();
                models.add(model);
            }
            case FLAT -> {
                FlatDirectionalLightingModel model = new FlatDirectionalLightingModel();
                model.direction = new Vector3f(0, -1, 0);
                models.add(model);

            }
            case SMOOTH -> {
                SmoothDirectionalLightingModel model = new SmoothDirectionalLightingModel();
                model.direction = new Vector3f(0, -1, 0);
                models.add(model);
            }
            case NONE -> {
                return;
            }
        }
    }
}
