package org.openjfx.connect_4.Logik;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author kevin.wolf
 * Nachfolgende Klasse lässte einen Timer starten bzw. stoppen, benutzereingabe entscheidet über die länge 
 */


public class GameClock {
	int time;
	Timer timer = new Timer();
	private Runnable action, onZero;
	
	
	public GameClock(int time, Runnable action) {
		this.time = time;
		this.action = action;
	}	
	public GameClock(int time) {
		this.time = time;
	}
	/**
	 * Nachfolgende Klasse startet den Timer und bricht ab, wenn die Zeit abgelaufen ist
	 * Probleme: wieder ins programmieren reinkommen, erst mit for schleife dann normal so gelöst
	 */
	
	public void start() {
		//Timer timer = new Timer();
		timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				time--;
				action.run();
				if(time == 0) {
					onZero.run();
					this.cancel();	
				}
			}
		
		}, 1000, 1000);
	/**
	 * Nachfolgende Klasse stoppt den Timer 
	 * //Probleme hier waren herauszufinden, welche Klasse den timer stoppt (es gibt keine, der Timer wird nun erst abgespeichert und dann beendet)
	 */
		
	}
	public void stop() {	
		timer.cancel();
	}	
	
	public void reset(int time) {
	this.time = time;
		
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public void setOnZero(Runnable action) {
		this.onZero = action;
	}
	
	public String toString() {
		int minutes = time / 60;
		int seconds = time % 60;
		
		String _time = "";
		if(minutes < 10) _time += "0";
		_time += String.valueOf(minutes) + " : ";
		if(seconds < 10) _time += "0";
		_time += String.valueOf(seconds);
		
		return _time;
	}
}
