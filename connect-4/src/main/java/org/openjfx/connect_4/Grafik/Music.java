package org.openjfx.connect_4.Grafik;

import java.net.URISyntaxException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {

	/**
	 * @author Endrit / Tom
	 * @param path
	 * @param cycle
	 */
	public static void sound(String path, boolean cycle, double volume) {
		Media sound;
		try {
			sound = new Media(Music.class.getResource(path).toURI().toString());
			MediaPlayer mediaPlayer  = new MediaPlayer(sound);
			mediaPlayer.setVolume(volume);
			mediaPlayer.play();
			if(cycle) 
				mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} 
	}
}