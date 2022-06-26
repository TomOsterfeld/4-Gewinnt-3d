package org.openjfx.connect_4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import org.openjfx.connect_4.Grafik.GameEnvironment;
import org.openjfx.connect_4.Grafik.Music;
import org.openjfx.connect_4.Grafik.SceneController;
import org.openjfx.connect_4.Logik.ConsolePlayer;
import org.openjfx.connect_4.Logik.Game;
import org.openjfx.connect_4.Logik.Game.GameStage;
import org.openjfx.connect_4.Logik.Move;
import org.openjfx.connect_4.Logik.Player;

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
        //scene = new Scene(loadFXML("startmenu"), 1000, 600);
    	
        stage.setTitle("Connect 4 3D");
        
        // set icon
        stage.getIcons()
                .add(new javafx.scene.image
                .Image(Objects.requireNonNull(getClass().getResource("/img/Connect_4_icon.png")).toExternalForm()));
    	
    	//GameEnvironment gameEnvironment = new GameEnvironment();
    	//scene = gameEnvironment.getScene();
        //stage.setScene(scene);
        
        stage.setOnCloseRequest(windowEvent -> exit());
        
    	SceneController.switchScene("START_MENU", "startmenu", false); // switch to startmenu
        
        stage.show();
    }

    public static void main(String[] args) {
    	Music.sound("/sounds/KahootMusic .mp3", true, 1);
    	//startConsoleGame();
    	launch();
    }
    
    public static void startConsoleGame() {
    	Game game = new Game(5, 5, 5, 4, new ConsolePlayer(), new ConsolePlayer());
    	
    	while(game.getCurrentGameStage().equals(GameStage.GAME_NOT_ENDED)) {
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