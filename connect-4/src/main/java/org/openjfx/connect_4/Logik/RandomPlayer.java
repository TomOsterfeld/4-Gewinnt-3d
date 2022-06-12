package org.openjfx.connect_4.Logik;

import java.util.List;

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
		List<Move> valideMoves = game.getValideMoves();
		
		int randomIndex = (int) (Math.random() * valideMoves.size()); 
		
		return valideMoves.get(randomIndex);
	}
	
	@Override
	public String toString() {
		return "Random Player";
	}	
}
