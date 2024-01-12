module org.openjsr.main {
    requires cg.vsu.math;
    requires javafx.controls;
    requires javafx.fxml;

    exports org.openjsr.app;
    exports org.openjsr.app.node;

    opens org.openjsr.app;
}
