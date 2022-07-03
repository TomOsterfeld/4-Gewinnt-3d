module org.openjfx.connect_4 {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.media;
	requires java.prefs;
	// requires javafx.base;

	opens org.openjfx.connect_4 to javafx.fxml;

	exports org.openjfx.connect_4;
}
