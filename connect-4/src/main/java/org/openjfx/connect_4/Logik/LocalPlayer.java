package org.openjfx.connect_4.Logik;

public class LocalPlayer extends Player {
	@Override
	public synchronized Move getMove() {
		synchronized(this) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return move;
	}
	
}
