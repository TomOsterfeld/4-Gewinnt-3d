package org.openjfx.connect_4;

import java.util.prefs.Preferences;

import org.openjfx.connect_4.Grafik.SceneController;
import org.openjfx.connect_4.Logik.BotPlayer;
import org.openjfx.connect_4.Logik.ConsolePlayer;
import org.openjfx.connect_4.Logik.Game;
import org.openjfx.connect_4.Logik.LocalPlayer;
import org.openjfx.connect_4.Logik.Player;
import org.openjfx.connect_4.Logik.RandomPlayer;

import javafx.fxml.FXML;
import javafx.scene.control.SplitMenuButton;

/**
 * 
 * @author Tom
 *
 */
public class StartMenuController {
	@FXML
	private SplitMenuButton playerButton1, playerButton2;
	public static final Preferences PREFS = Preferences.userNodeForPackage(Main.class);

	/**
	 * wird aufgerufen wenn die Scene geladen wird
	 */
	public void initialize() {
		playerButton1.setText(PREFS.get("PLAYER_1", "Local Game"));
		playerButton2.setText(PREFS.get("PLAYER_2", "Local Game"));

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

		int x = PREFS.getInt("BOARD_X", 5);
		int y = PREFS.getInt("BOARD_Y", 5);
		int z = PREFS.getInt("BOARD_Z", 4);
		int winningLength = PREFS.getInt("WINNING_LENGTH", 4);

		PREFS.put("PLAYER_1", value1);
		PREFS.put("PLAYER_2", value2);

		switch (value1) {
		case "Random Player":
			player1 = new RandomPlayer();
			break;
		case "Computer Player":
			player1 = new BotPlayer();
			break;
		case "Console Player":
			player1 = new ConsolePlayer();
			break;
		default:
			player1 = new LocalPlayer();
		}

		switch (value2) {
		case "Random Player":
			player2 = new RandomPlayer();
			break;
		case "Computer Player":
			player2 = new BotPlayer();
			break;
		case "Console Player":
			player2 = new ConsolePlayer();
			break;
		default:
			player2 = new LocalPlayer();
		}

		Game game = new Game(x, y, z, winningLength, player1, player2);

		game.startGame();
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
