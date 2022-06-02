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
	
	private final int winningLength = 4;
	
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
	
	/**
	 * initialize all constants
	 * @param x: Spielbreite in x-Richtung
	 * @param y: Spielbreite in y-Richtung
	 * @param z: Spielhöhe
	 * @param playerRed: Der Spieler mit den roten Tokens
	 * @param playerYellow : Der Spieler mit den gelben Tokens
	 */
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
	
	/**
	 * führt einen übergebenen Move aus
	 * @param move: der auszuführende Move
	 * @return 	true: Der auszuführende Move wurde akzeptiert und ist zulässig
	 * 			false: Der auszuführende Move ist unzulässig
	 */
	public boolean doMove(Move move) {
		if(isValide(move)) {
			int x = move.getX(), y = move.getY(), z = heights[x][y];
			
			board[x][y][z] = redTurn ? Token.RED : Token.YELLOW; // setze den Token
			heights[x][y]++; // Erhöhe die Höhe um 1
			
			updateGameStage(move); // aktualisiere den Spielstatus
			
			redTurn = !redTurn; // der andere Spieler ist jetzt am Zug
			setCurrentPlayer(redTurn ? playerRed : playerYellow);
			
			System.out.println(rating);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Gibt zurück, ob ein Move zulässig ist
	 * @param move: der zu überprüfende Move
	 * @return	true:	der Zug ist zulässig
	 * 			false:	der Zug ist unzulässig
	 */
	public boolean isValide(Move move) {
		if(move.getX() < 0 || move.getX() >= x
				|| move.getY() < 0 || move.getY() >= y) return false; // Der Punkt mit den entsprechenden x-, y-Koordinaten ist außerhalb des Spielfeldes
		if(heights[move.getX()][move.getY()] >= z || currentStage != GameStage.GAME_NOT_ENDED) return false;
		return true;
	}
	
	/**
	 * 
	 * @param move: der übergebene Move
	 * @return 	true: 
	 * 			false:
	 */
	public boolean updateGameStage(Move move) {
		if(!updateRating(move)) {
			if(rating == 1000) {
				currentStage = GameStage.RED_WIN;
				System.out.println("Red wins");
			} else {
				currentStage = GameStage.YELLOW_WIN;
				System.out.println("Yellow wins");
			}
			return false;
		}
		return true;
	}
	
	public boolean updateRating(Move move) {
		List<List<Token>> rows = getRows(move);
		Token currentToken = redTurn ? Token.RED : Token.YELLOW; 
		
		rows.forEach(row -> {
			int tokenCounter = 0;
			int n = row.size();
			
			//System.out.println("size: " + n);
			
			for(int i = 0; i < n; i++) {
				if(row.get(i).equals(currentToken)) {
					tokenCounter++;
					if(tokenCounter == winningLength) rating = redTurn ? 1000 : -1000; // either player wins
				}
				else tokenCounter = 0;
			}
		});
		
		if(rating == 1000 || rating == -1000) {
			return false; // the rating has been updated so that there is either a win or a loss
		}
		
		return true;
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
					if(x == 0 && y == 0 && z == 0) continue; // at least one index needs to change
					
					int leftBoarder = winningLength - 1;
					int rightBoarder = winningLength - 1;
					
					int move_x = move.getX(), move_y = move.getY(), move_z = heights[move_x][move_y] - 1;
					
					if(x != 0) {
						if(x == 1) {
							leftBoarder = Math.min(leftBoarder, move_x);
							rightBoarder = Math.min(rightBoarder, this.x - move_x - 1);
						} else {
							leftBoarder = Math.min(leftBoarder, this.x - move_x - 1);
							rightBoarder = Math.min(rightBoarder, move_x);
						}
					}
					
					if(y != 0) {
						if(y == 1) {
							leftBoarder = Math.min(leftBoarder, move_y);
							rightBoarder = Math.min(rightBoarder, this.y - move_y - 1);
						} else {
							leftBoarder = Math.min(leftBoarder, this.y - move_y - 1);
							rightBoarder = Math.min(rightBoarder, move_y);
						}
					}
					
					if(z != 0) {
						if(z == 1) {
							leftBoarder = Math.min(leftBoarder, move_z);
							rightBoarder = Math.min(rightBoarder, this.z - move_z - 1);
						} else {
							leftBoarder = Math.min(leftBoarder, this.z - move_z - 1);
							rightBoarder = Math.min(rightBoarder, move_z);
						}
					}
					
					int rowLength = rightBoarder + leftBoarder + 1;
					
					if(rowLength < winningLength) continue; // row not long enough
					List<Token> row = new ArrayList(2 * winningLength - 1);
					
					int _x = move_x - x * leftBoarder;
					int _y = move_y - y * leftBoarder;
					int _z = move_z - z * leftBoarder;
					
					for(int i = 0; i < rowLength; _x += x, _y += y, _z += z, i++) {
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
