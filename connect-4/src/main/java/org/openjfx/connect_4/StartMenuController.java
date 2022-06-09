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
		
	}
	
    @FXML
    private void onStartButtonClicked() {
    	Player player1, player2;
    	
    	String value1 = "Local Player";//playerButton1.getPopupSide();
    	String value2 = "Local Player";
    	
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
    	
    	Game game = new Game(5, 5, 5, 4, player1, player2);
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
