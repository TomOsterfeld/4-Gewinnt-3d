package org.openjfx.connect_4.Logik;

public class BotRandom extends Player {
	Game game;
	
	public BotRandom(Game game) {
		this.game = game;
	}
	
	/**
	 * @return : zufälliger Move
	 */
	@Override
	public Move getMove() {
		int x = game.board.length;
		int y = game.board[0].length;
		
		int _x = (int) (Math.random() * x);
		int _y = (int) (Math.random() * y);
		
		return new Move(_x, _y);
	}
}
