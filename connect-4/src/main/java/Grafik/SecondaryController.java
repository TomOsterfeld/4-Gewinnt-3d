package Grafik;

import java.io.IOException;

import org.openjfx.connect_4.App;

import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}