package org.openjfx.connect_4;

import java.util.prefs.Preferences;

import org.openjfx.connect_4.Grafik.GameEnvironment;
import org.openjfx.connect_4.Grafik.SceneController;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * 
 * @author Tom
 *
 */
public class SettingsMenuController {
	@FXML 
	TextField timeTextField, botTimeTextField, xTextField, yTextField, zTextField, winningLengthTextField;
	
	public void initialize() {
    	Preferences PREFS = StartMenuController.PREFS;
		
    	// erhalte die gespeicherten Werte und setze den Text der Textfelder darauf
    	int time = PREFS.getInt("TIME", 5);
    	int botTime = PREFS.getInt("BOT_TIME", 2);
    	int x = PREFS.getInt("BOARD_X", 5);
    	int y = PREFS.getInt("BOARD_Y", 5);
    	int z = PREFS.getInt("BOARD_Z", 4);
    	int winningLength = PREFS.getInt("WINNING_LENGTH", 4);
    	
    	timeTextField.setText(String.valueOf(time));
    	botTimeTextField.setText(String.valueOf(botTime));
    	xTextField.setText(String.valueOf(x));
    	yTextField.setText(String.valueOf(y));
    	zTextField.setText(String.valueOf(z));
    	winningLengthTextField.setText(String.valueOf(winningLength));
	}
	
    @FXML
    private void onStartMenuButtonClicked() {
    	Preferences PREFS = StartMenuController.PREFS;
    	// Speicher die Werte der Textfelder ab, wenn man das Menü verlässt
    		
    	PREFS.putInt("TIME", getInt(timeTextField.getText(), 1, 59));
    	PREFS.putInt("BOT_TIME", getInt(botTimeTextField.getText(), 1, 59));
    	PREFS.putInt("BOARD_X", getInt(xTextField.getText(), 1, 10));
    	PREFS.putInt("BOARD_Y", getInt(yTextField.getText(), 1, 10));
    	PREFS.putInt("BOARD_Z", getInt(zTextField.getText(), 1, 10));
    	PREFS.putInt("WINNING_LENGTH", getInt(winningLengthTextField.getText(), 1, 10));
    	
    	SceneController.switchScene("START_MENU", "settingsmenu", false);
    }
    
    /**
     * wandle String in Integer um, der zwischen min und max liegt
     */
    private int getInt(String value, int min, int max) {
    	int integer = min;
    	
    	try {
    		integer = Integer.valueOf(value);
    	} catch(NumberFormatException ignored) {}
    	
    	integer = Math.max(min, integer);
    	integer = Math.min(max, integer);
    	return integer;
    }
    
    @FXML
    private void onClassicButtonClicked() {
    	xTextField.setText(String.valueOf(5));
    	yTextField.setText(String.valueOf(5));
    	zTextField.setText(String.valueOf(4));
    }
    
    @FXML
    private void onSmallButtonClicked() {
    	xTextField.setText(String.valueOf(4));
    	yTextField.setText(String.valueOf(4));
    	zTextField.setText(String.valueOf(4));
    }
}
