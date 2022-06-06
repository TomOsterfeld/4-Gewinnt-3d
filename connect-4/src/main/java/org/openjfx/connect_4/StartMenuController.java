package org.openjfx.connect_4;

import org.openjfx.connect_4.Grafik.GameEnvironment;
import org.openjfx.connect_4.Grafik.SceneController;
import org.openjfx.connect_4.Logik.ConsolePlayer;
import org.openjfx.connect_4.Logik.Game;
import org.openjfx.connect_4.Logik.LocalPlayer;
import org.openjfx.connect_4.Logik.RandomPlayer;

import javafx.fxml.FXML;

public class StartMenuController {
    @FXML
    private void onStartButtonClicked() {
    	Game game = new Game(10, 10, 7, 9, new RandomPlayer(), new RandomPlayer());
    	Game.startGame(game);
    }
    
    @FXML
    private void onSettingsButtonClicked() {
    	SceneController.switchScene("SETTINGS_MENU", "settingsmenu", false);
    }
    
    @FXML
    private void onExitButtonClicked() {
    	Main.exit();
    }
}
