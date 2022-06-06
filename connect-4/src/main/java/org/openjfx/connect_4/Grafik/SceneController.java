package org.openjfx.connect_4.Grafik;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import org.openjfx.connect_4.Main;

public class SceneController {
    private static Stage stage;
    private static HashMap<String, Scene> SCENE_HASH_MAP = new HashMap<>(5);
    private static HashMap<String, Boolean> RESIZEBLE_MAP = new HashMap<>(5);

    /**
     * switches the stages scene
     *
     * @param sceneName: the name of the new scene
     * @param path:      the path to the fxml file of the new scene
     */
    public static void switchScene(String sceneName, String path, boolean resizable) {
        //FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getClassLoader().getResource(path + ".fxml"));
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
     * @param scene:     the scene to be switched to
     */
    public static void switchScene(String sceneName, Scene scene) {
        switchScene(sceneName, scene, true);
    }

    public static void switchScene(String sceneName) {
        switchScene(sceneName, new Scene(new Group()), RESIZEBLE_MAP.get(sceneName));
    }

    public static void switchScene(String sceneName, Scene scene, boolean resizable) {
        if (!SCENE_HASH_MAP.containsKey(sceneName)) {
            SCENE_HASH_MAP.put(sceneName, scene);
            RESIZEBLE_MAP.put(sceneName, resizable);
        }

        stage.setScene(SCENE_HASH_MAP.get(sceneName));
    	System.out.println("Switch to " + sceneName);
        stage.setResizable(resizable);
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