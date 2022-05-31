package org.openjfx.connect_4.Logik;

/**
 * 
 * @author Tom
 *
 */
public class RandomPlayer extends Player {	
	public RandomPlayer(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	public RandomPlayer() {}

	/**
	 * @return : Move mit zufälligen Koordinaten innerhalb des Spielfeldes
	 */
	@Override
	public Move getMove() {	// TODO: sicherstellen, dass Move gültig ist.	
		int x = (int) (Math.random() * super.x); // zufällige y-Koordinate
		int y = (int) (Math.random() * super.y); // zufällige x-Koordinate
		
		return new Move(x, y);
	}
}
