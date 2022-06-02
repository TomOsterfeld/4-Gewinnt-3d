package Grafik;

import java.io.IOException;

import org.openjfx.connect_4.App;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
