package org.openjfx.connect_4.Logik;

public class Game {
	public final Token[][][] board;
	private Player player1, player2;
	
	private GameStage currentStage;
	
	enum GameStage {
		GAME_NOT_ENDED, RED_WIN, YELLOW_WIN, DRAW
	}
	
	enum Token {
		RED, YELLOW, NONE;
	}
	
	public Game(int x, int y, int z, Player player1, Player player2) {
		board = new Token[x][y][z];
		this.player1 = player1;
		this.player2 = player2;
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				for(int k = 0; k < board[0][0].length; k++) {
					board[i][j][k] = Token.NONE;
				}
			}
		}
	}
	
	public boolean doMove(Move move) {
		if(isValide(move)) {
			updateGameStage(move);
			return true;
		}
		return false;
	}
	
	public boolean isValide(Move move) {
		return true;
	}
	
	public boolean updateGameStage(Move move) {
		return true;
	}
	
	public void showGame() {
		
	}
}
