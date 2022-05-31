package org.openjfx.connect_4.Logik;

public abstract class Player {
	protected Game game;
	protected int x;
	protected int y;
	
	public Player(Game game) {
		this.game = game;
		x = game.getX();
		y = game.getY();
	}
	
	public Player() {}
	
	public abstract Move getMove();
	
	public void setGame(Game game) {
		this.game = game;
	}
}
