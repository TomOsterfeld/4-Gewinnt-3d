package org.openjfx.connect_4.Logik;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Tom
 *
 */
public class Game {
	private final int x, y, z;
	
	private final Token[][][] board; // store tokens
	private final int[][] heights; // store the height at each location
	
	private Player playerRed;
	private Player playerYellow;
	private Player currentPlayer;
	
	private boolean redTurn;
	
	private GameStage currentStage;
	
	private Move latestMove;
	private Move secondLatestMove;
	
	private int rating;
	
	public enum GameStage {
		GAME_NOT_ENDED, RED_WIN, YELLOW_WIN, DRAW
	}
	
	public enum Token {
		RED(" x "), YELLOW(" o "), NONE(" - ");
		String symbol;
		
		Token(String symbol) {
			this.symbol = symbol;
		}
		
		@Override
		public String toString() {
			return symbol;
		}
	}
	
	public Game(int x, int y, int z, Player playerRed, Player playerYellow) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.board = new Token[x][y][z];
		this.heights = new int[x][y];
		
		this.playerRed = playerRed;
		this.playerYellow = playerYellow;
		
		init(x, y, z, playerRed, playerYellow);
	}
	
	public void init(int x, int y, int z, Player playerRed, Player playerYellow) {		
		playerRed.setGame(this); // set the players games to this one
		playerYellow.setGame(this);
		
		rating = 0;
		
		setCurrentPlayer(playerRed);
		currentStage = GameStage.GAME_NOT_ENDED;
		
		// create empty board
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				for(int k = 0; k < board[0][0].length; k++) {
					board[i][j][k] = Token.NONE;
				}
			}
		}
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				 heights[i][j] = 0;
			}
		}
		
		redTurn = true; // red moves first
	}
	
	public boolean doMove(Move move) {
		if(isValide(move)) {
			int x = move.getX(), y = move.getY(), z = heights[x][y];
			
			board[x][y][z] = redTurn ? Token.RED : Token.YELLOW; // put new Token into Board
			heights[x][y]++; // update height
			
			updateGameStage(move);
			
			redTurn = !redTurn;
			setCurrentPlayer(redTurn ? playerRed : playerYellow);
			
			return true;
		}
		return false;
	}
	
	public boolean isValide(Move move) {
		if(move.getX() < 0 || move.getX() >= x || move.getY() < 0 || move.getY() >= y) return false; // Der Punkt mit den entsprechenden x-, y-Koordinaten ist außerhalb des Spielfeldes
		if(heights[move.getX()][move.getY()] >= z || currentStage != GameStage.GAME_NOT_ENDED) return false;
		return true;
	}
	
	public boolean updateGameStage(Move move) {
		return updateRating(move);
	}
	
	public boolean updateRating(Move move) {
		List<List<Token>> rows = getRows(move);
		
		rows.forEach(row -> {
			
		});
		
		return false;
	}
	
	/**
	 * 
	 * @param move
	 * @return
	 */
	public List<List<Token>> getRows(Move move) {
		List<List<Token>> rows = new ArrayList();
		
		for(int x = -1; x <= 0; x++) {
			for(int y = -1; y <= 0; y++) {
				for(int z = -1; z <= 0; z++) {
					int leftBoarder = 3;
					int rightBoarder = 3;
					
					int rowLength = rightBoarder + leftBoarder + 1;
					
					if(rowLength < 4) continue; // row not long enough
					List<Token> row = new ArrayList(7);
					
					int _x = move.getX() - x * leftBoarder;
					int _y = move.getY() - y * leftBoarder;
					int _z = heights[move.getX()][move.getY()] - z * leftBoarder;
					
					for(int i = 0; i < rowLength; _x += x, _y += y, _z += z) {
						if(_x > this.x || _y > this.y || _z > this.z || _z < 0 || _y < 0 || _x < 0)
							continue;
						row.add(board[_x][_y][_z]);
					}
					
					rows.add(row);
				}
			}
		}
		
		return rows;
	}
	
	@Override
	public String toString() {
		String toString = (redTurn ? "Rot" : "Gelb") + " ist am Zug\n";
		
        for(int i = 0; i < z; i++) {
            for(int j = 0; j < getX(); j++) {
                for(int k = 0; k < getY(); k++) {
                    toString += " " + board[j][k][getY() - i - 1] + " ";
                }
                toString += "   ";
            }
            toString += "\n";
        }
        toString += "\n\n\n";
		
		return toString;
	}
	
	public GameStage getCurrentGameStage() {
		return currentStage;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}
