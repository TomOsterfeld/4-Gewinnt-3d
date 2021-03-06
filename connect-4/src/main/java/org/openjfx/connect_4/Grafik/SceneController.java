package org.openjfx.connect_4.Grafik;

import java.io.IOException;
import java.util.HashMap;

import org.openjfx.connect_4.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Tom
 *
 */
public class SceneController {
	private static Stage stage;
	private static HashMap<String, Scene> SCENE_HASH_MAP = new HashMap<>(5);
	private static HashMap<String, Boolean> RESIZEBLE_MAP = new HashMap<>(5);

	/**
	 * switches the stages scene
	 *
	 * @param sceneName: Der Name der Scene
	 * @param path:      Der Pfad des fxml-files
	 */
	public static void switchScene(String sceneName, String path, boolean resizable) {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path + ".fxml"));

		if (!SCENE_HASH_MAP.containsKey(sceneName)) {
			try {
				Scene scene = new Scene(fxmlLoader.load());
				SCENE_HASH_MAP.put(sceneName, scene);
				RESIZEBLE_MAP.put(sceneName, resizable);
			} catch (IOException exc) {
				exc.printStackTrace();
			}
		}

		stage.setScene(SCENE_HASH_MAP.get(sceneName));
		System.out.println("Switch to " + sceneName);
		stage.setResizable(resizable);
	}

	/**
	 * @param sceneName: the name of the new scene
	 * @param scene:     the scene to be switched to Wenn eine Scene mit �bergeben
	 *                   wird, soll diese immer neu geladen werden und nicht die der
	 *                   HashMap angezeigt werden
	 */
	public static void switchScene(String sceneName, Scene scene) {
		switchScene(sceneName, scene, true, true);
	}

	public static void switchScene(String sceneName) {
		switchScene(sceneName, null, false, false);
	}

	/**
	 * @param: newScene: soll Scene neu geladen werden
	 */
	public static void switchScene(String sceneName, Scene scene, boolean resizable, boolean newScene) {
		if (!SCENE_HASH_MAP.containsKey(sceneName) || newScene) {
			SCENE_HASH_MAP.put(sceneName, scene);
			RESIZEBLE_MAP.put(sceneName, resizable);
		}

		stage.setScene(SCENE_HASH_MAP.get(sceneName));
		System.out.println("Switch to " + sceneName);
		stage.setResizable(RESIZEBLE_MAP.get(sceneName));
		stage.centerOnScreen();
	}

	public static void setResizable(boolean resizable) {
		stage.setResizable(resizable);
	}

	/**
	 * remove scene from hashMap
	 *
	 * @param sceneName: name of the scene
	 */
	public static void removeScene(String sceneName) {
		SCENE_HASH_MAP.remove(sceneName);
	}

	public static Scene getCurrentScene() {
		return stage.getScene();
	}

	public static void setStage(Stage stage) {
		SceneController.stage = stage;
	}

	public static Stage getStage() {
		return stage;
	}
}