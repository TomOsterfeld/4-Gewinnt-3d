package org.openjfx.connect_4.Logik;

/**
 * 
 * @author Tom
 *
 */
public abstract class Player {
	protected Game game;
	protected int x;
	protected int y;
	protected Move move;

	public Player(Game game) {
		this.game = game;
		x = game.getX();
		y = game.getY();
	}

	public Player() {
	}

	public abstract Move getMove();

	public void setGame(Game game) {
		this.game = game;
	}

	public void setMove(Move move) {
		this.move = move;
	}
}
