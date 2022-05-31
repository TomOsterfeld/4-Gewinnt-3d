package org.openjfx.connect_4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.openjfx.connect_4.Logik.ConsolePlayer;
import org.openjfx.connect_4.Logik.Game;
import org.openjfx.connect_4.Logik.Game.GameStage;
import org.openjfx.connect_4.Logik.Move;
import org.openjfx.connect_4.Logik.Player;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        //launch();
    	
    	startConsoleGame();
    }
    
    public static void startConsoleGame() {
    	Game game = new Game(4, 4, 4, new ConsolePlayer(), new ConsolePlayer());
    	
    	while(game.getCurrentGameStage().equals(GameStage.GAME_NOT_ENDED)) {
        	System.out.println(game);
    		Move move = game.getCurrentPlayer().getMove();
    		game.doMove(move);
    		
    	}
    }
   
}