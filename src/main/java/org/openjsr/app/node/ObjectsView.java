package org.openjsr.app.node;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ObjectsView extends TreeView<String> {
    private final TreeItem<String> modelsRoot = new TreeItem<>("Модели");

    private final TreeItem<String> camerasRoot = new TreeItem<>("Камеры");

    public ObjectsView() {
        super();
        setShowRoot(false);
        TreeItem<String> root = new TreeItem<>(null);
        setRoot(root);

        root.getChildren().add(modelsRoot);
        root.getChildren().add(camerasRoot);
        modelsRoot.setExpanded(true);
        camerasRoot.setExpanded(true);

        var modelList = modelsRoot.getChildren();
        modelList.add(new TreeItem<>("Model 1"));
        modelList.add(new TreeItem<>("Model 2"));
        modelList.add(new TreeItem<>("Model 3"));

        var cameraList = camerasRoot.getChildren();
        cameraList.add(new TreeItem<>("Camera 1"));
        cameraList.add(new TreeItem<>("Camera 2"));
        cameraList.add(new TreeItem<>("Camera 3"));
    }
}
