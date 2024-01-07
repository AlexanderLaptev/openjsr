package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.openjsr.render.lighting.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LightMenu extends VBox {

    public List<LightingModel> models;

    public LightMenu() {
        models = new ArrayList<>();
        Button addButton = new Button("Добавить модель освещения");
        addButton.setOnAction(e -> {
            int input = addLightningModel();
            chooseModel(input);
        });
        getChildren().add(addButton);
    }

    public int addLightningModel() {
        Dialog<Integer> addDialog = new Dialog<>();
        addDialog.setTitle("Выберите тип модели освещения");


        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        addDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        ComboBox<String> typesBox = new ComboBox<>();
        typesBox.setItems(FXCollections.observableList(Arrays.asList("Равномерное освещение", "Плоское освещение", "Гладкое освещение")));

        addDialog.getDialogPane().setContent(typesBox);

        addDialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return typesBox.getVisibleRowCount();
            }
            return null;
        });

        return addDialog.showAndWait().get();
    }

    public void chooseModel(Integer input) {
        LightingModel model;
        switch (input) {
            case 0 -> {
                model = new UniformLightingModel();
                showIntensityDialog(model, UniformLightingModel.DEFAULT_INTENSITY);
            }
            case 1 -> {
                model = new FlatDirectionalLightingModel();
                showIntensityDialog(model, DirectionalLightingModel.DEFAULT_AMBIENT_LIGHT_LEVEL);
                showDirectionDialog((DirectionalLightingModel) model, new Vector3f(0, -1, 0));
            }
            case 2 -> {
                model = new SmoothDirectionalLightingModel();
                showIntensityDialog(model, DirectionalLightingModel.DEFAULT_AMBIENT_LIGHT_LEVEL);
                showDirectionDialog((DirectionalLightingModel) model, new Vector3f(0, -1, 0));
            }
            default -> {
                return;
            }
        }
        models.add(model);
    }

    public void showIntensityDialog(LightingModel model, float defaultIntensity) {
        Dialog<Float> intensityDialog = new Dialog<>();
        intensityDialog.setTitle("Выберите интенсивность");

        TextField field = new TextField(String.valueOf(defaultIntensity));
        intensityDialog.getDialogPane().setContent(field);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        intensityDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        intensityDialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                float res;
                try {
                    res = Float.parseFloat(field.getText());
                } catch (Exception ignored) {
                    res = defaultIntensity;
                }
                return res;
            }
            return defaultIntensity;
        });

        intensityDialog.showAndWait().ifPresent(model::setIntensity);
    }

    public void showDirectionDialog(DirectionalLightingModel model, Vector3f def) {
        Dialog<Vector3f> directionDialog = new Dialog<>();
        directionDialog.setTitle("Выберите направление источника света");

        VectorTextField field = new VectorTextField("Направление", def.x, def.y, def.z);
        directionDialog.getDialogPane().setContent(field);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        directionDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        directionDialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return field.getVector();
            }
            return def;
        });

        directionDialog.showAndWait().ifPresent(res -> {
            model.direction = res;
        });
    }

}
