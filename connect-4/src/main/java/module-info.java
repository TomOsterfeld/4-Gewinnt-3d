module org.openjfx.connect_4 {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.openjfx.connect_4 to javafx.fxml;
    exports org.openjfx.connect_4;
}
