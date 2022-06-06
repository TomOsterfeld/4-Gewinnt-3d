package org.openjfx.connect_4.Logik;

/**
 * 
 * @author Tom
 *
 */
public class RandomPlayer extends Player {	
	public RandomPlayer(Game game) {
		super(game);
	}
	
	public RandomPlayer() {}

	/**
	 * @return : Move mit zufälligen Koordinaten innerhalb des Spielfeldes
	 */
	@Override
	public Move getMove() {	// TODO: sicherstellen, dass Move gültig ist.	
		int x = (int) (Math.random() * game.getX()); // zufällige y-Koordinate
		int y = (int) (Math.random() * game.getY()); // zufällige x-Koordinate
		
		return new Move(x, y);
	}
}
