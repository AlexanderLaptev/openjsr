package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjsr.render.lighting.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javafx.collections.FXCollections.*;

/**
 * Класс - хранилище созданных пользователем моделей освещения.
 */
public class LightStorage {

    /**
     * Список всех моделей освещения.
     */
    public List<LightingModel> models = new ArrayList<>();

    public LightStorage() {
    }

    /**
     * Вызывает диалоговое окно с предложением выбрать тип освещения.
     *
     * @return номер строки в списке, которую выбрал пользователь.
     */
    public int addLightningModelDialog() {
        Dialog<Integer> addDialog = new Dialog<>();

        String cssPath = getClass().getResource("/stylesheets/dialog-panel.css").toExternalForm();
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

        return addDialog.showAndWait().get();
    }

    /**
     * Выбирает тип модели из доступных.
     *
     * @param input номер модели в списке.
     */
    public void chooseModel(Integer input) {

        switch (input) {
            case 0 -> {
                UniformLightingModel model = new UniformLightingModel();
                models.add(model);
            }
            case 1 -> {
                FlatDirectionalLightingModel model = new FlatDirectionalLightingModel();
                model.direction = new Vector3f(0, -1, 0);
                models.add(model);

            }
            case 2 -> {
                SmoothDirectionalLightingModel model = new SmoothDirectionalLightingModel();
                model.direction = new Vector3f(0, -1, 0);
                models.add(model);
            }
            default -> {
                return;
            }
        }

    }
}
