package org.openjfx.connect_4;

import java.io.IOException;
import java.util.Objects;

import org.openjfx.connect_4.Grafik.Music;
import org.openjfx.connect_4.Grafik.SceneController;
import org.openjfx.connect_4.Logik.ConsolePlayer;
import org.openjfx.connect_4.Logik.Game;
import org.openjfx.connect_4.Logik.Game.GameStage;
import org.openjfx.connect_4.Logik.Move;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Tom
 *
 */
public class Main extends Application {

	private static Scene scene;
	private SceneController sceneController;

	@Override
	public void start(Stage stage) throws IOException {
		SceneController.setStage(stage);

		stage.setTitle("Connect 4 3D");

		// set icon
		stage.getIcons().add(new javafx.scene.image.Image(
				Objects.requireNonNull(getClass().getResource("/img/Connect_4_icon.png")).toExternalForm()));

		stage.setOnCloseRequest(windowEvent -> exit());

		SceneController.switchScene("START_MENU", "startmenu", false); // switch to startmenu

		stage.show();
	}

	public static void main(String[] args) {
		Music.sound("/sounds/kahoot-music.wav", true, 1);
		// startConsoleGame();
		launch();
	}

	public static void startConsoleGame() {
		Game game = new Game(5, 5, 5, 4, new ConsolePlayer(), new ConsolePlayer());

		while (game.getCurrentGameStage().equals(GameStage.GAME_NOT_ENDED)) {
			System.out.println(game);
			Move move = game.getCurrentPlayer().getMove();
			game.doMove(move);
		}

		System.out.println(game);
	}

	public static void exit() {
		System.out.println("Application terminated!");
		System.exit(0);
	}

}