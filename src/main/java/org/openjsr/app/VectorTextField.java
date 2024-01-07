package org.openjsr.app;

import cg.vsu.render.math.vector.Vector3f;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VectorTextField extends VBox {

    public TextField xField;
    public TextField yField;
    public TextField zField;


    public VectorTextField(String name, float x, float y, float z) {
        Label nameLabel = new Label(name);
        HBox boxX = new HBox();
        Label xlabel = new Label("X:");
        xField = new TextField(String.valueOf(x));
        boxX.getChildren().addAll(xlabel, xField);

        HBox boxY = new HBox();
        Label ylabel = new Label("Y:");
        yField = new TextField(String.valueOf(y));
        boxY.getChildren().addAll(ylabel, xField);

        HBox boxZ = new HBox();
        Label zlabel = new Label("Y:");
        zField = new TextField(String.valueOf(z));
        boxZ.getChildren().addAll(zlabel, zField);

        getChildren().add(nameLabel);
        getChildren().addAll(boxX, boxY, boxZ);
    }

    public Vector3f getVector() {
        float x = Float.parseFloat(xField.getText().replaceAll("[^0-9]", ""));
        float y = Float.parseFloat(yField.getText().replaceAll("[^0-9]", ""));
        float z = Float.parseFloat(zField.getText().replaceAll("[^0-9]", ""));

        return new Vector3f(x, y, z);
    }

    public void setOnTyped(EventHandler<? super KeyEvent> value) {
        xField.setOnKeyTyped(value);
        yField.setOnKeyTyped(value);
        zField.setOnKeyTyped(value);
    }
}
