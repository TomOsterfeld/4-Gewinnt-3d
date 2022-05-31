package org.openjfx.connect_4.Logik;

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
		return false;
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
