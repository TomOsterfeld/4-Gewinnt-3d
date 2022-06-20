package org.openjfx.connect_4;

import org.openjfx.connect_4.Grafik.GameEnvironment;
import org.openjfx.connect_4.Grafik.SceneController;
import org.openjfx.connect_4.Logik.ConsolePlayer;
import org.openjfx.connect_4.Logik.Game;
import org.openjfx.connect_4.Logik.LocalPlayer;
import org.openjfx.connect_4.Logik.RandomPlayer;
import org.openjfx.connect_4.Logik.BotPlayer;
import org.openjfx.connect_4.Logik.Player;

import javafx.scene.control.SplitMenuButton;

import javafx.fxml.FXML;

public class StartMenuController {
	@FXML 
	private SplitMenuButton playerButton1, playerButton2;
	
	public void initialize() {
		playerButton1.setText("Local Game");
		playerButton2.setText("Local Game");
		
		playerButton1.getItems().forEach(item -> {
			item.setOnAction(action -> {
				playerButton1.setText(item.getText());
			});
		});
		
		playerButton2.getItems().forEach(item -> {
			item.setOnAction(action -> {
				playerButton2.setText(item.getText());
			});
		});
	}
	
    @FXML
    private void onStartButtonClicked() {
    	Player player1, player2;
    	
    	String value1 = playerButton1.getText();
    	String value2 = playerButton2.getText();
    	
    	switch(value1) {
    		case "Random Player": 
    			player1 = new RandomPlayer(); break;
    		case "Computer Player": 
    			player1 = new BotPlayer(); break;
    		case "Console Player": 
    			player1 = new ConsolePlayer(); break;
    		default: 
    			player1 = new LocalPlayer();
    	}
    	
    	switch(value2) {
			case "Random Player": 
				player2 = new RandomPlayer(); break;
			case "Computer Player": 
				player2 = new BotPlayer(); break;
			case "Console Player": 
				player2 = new ConsolePlayer(); break;
			default: 
				player2 = new LocalPlayer();
    	}
    	
    	Game game = new Game(5, 5, 4, 4, player1, player2);
    	
    	game.startGame();
    }
    
    @FXML
    private void onSettingsButtonClicked() {
    	SceneController.switchScene("SETTINGS_MENU", "settingsmenu", false, true);
    }
    
    @FXML
    private void onExitButtonClicked() {
    	Main.exit();
    }
}
