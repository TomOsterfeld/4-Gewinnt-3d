package org.openjfx.connect_4;

import org.openjfx.connect_4.Grafik.GameEnvironment;
import org.openjfx.connect_4.Grafik.SceneController;

import javafx.fxml.FXML;

public class SettingsMenuController {
    @FXML
    private void onStartMenuButtonClicked() {
    	SceneController.switchScene("START_MENU", "settingsmenu", false);
    }
}
