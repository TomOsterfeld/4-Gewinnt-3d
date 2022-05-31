package org.openjfx.connect_4.Logik;

/**
 * 
 * @author Tom
 *
 */
public class Move {
	private int x, y;
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getY() {
		return y;
	}
	
	public int getX() {
		return x;
	}
}
