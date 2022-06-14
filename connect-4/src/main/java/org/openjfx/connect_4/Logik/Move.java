package org.openjfx.connect_4.Logik;

/**
 * 
 * @author Tom
 *
 */
public class Move {
	private int x, y;
	private int rating;
	
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
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
}
