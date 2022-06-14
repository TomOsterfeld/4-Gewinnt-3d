package org.openjfx.connect_4.Logik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.openjfx.connect_4.Grafik.GameEnvironment;
import org.openjfx.connect_4.Grafik.SceneController;

import javafx.application.Platform;

/**
 * 
 * @author Tom
 *
 */
public class Game {
	private final int x, y, z;
	
	private final Token[][][] board; // store tokens
	private final int[][] heights; // store the height at each location
	
	private final int winningLength;
	
	private Player playerRed;
	private Player playerYellow;
	private Player currentPlayer;
	
	private boolean redTurn;
	
	private GameStage currentStage;
	
	private int rating;
	
	private CopyOnWriteArrayList <Move> valideMoves;
	
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
	
	public Game(int x, int y, int z, int winningLength, Player playerRed, Player playerYellow) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.winningLength = winningLength;
		
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
		
		setRating(0);
		
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
		
		valideMoves = new CopyOnWriteArrayList<Move>();
		
		for(int i = 0; i < this.x; i++) {
			for(int j = 0; j < this.y; j++) {
				valideMoves.add(new Move(i, j));
			}
		}
		
		presortValideMoves();
		
		redTurn = true; // red moves first
		rating = 0;
	}
	
	/**
	 * führt einen übergebenen Move aus
	 * @param 	move: der auszuführende Move
	 * @return 	true: Der auszuführende Move wurde akzeptiert und ist zulässig
	 * 			false: Der auszuführende Move ist unzulässig
	 */
	public boolean doMove(Move move) {
		if(isValide(move)) {
			int x = move.getX(), y = move.getY(), z = heights[x][y];
			
			board[x][y][z] = isRedTurn() ? Token.RED : Token.YELLOW; // setze den Token
			heights[x][y]++; // Erhöhe die Höhe um 1
			
			if(heights[x][y] == this.z) 
				getValideMoves().removeIf(removedMove -> removedMove.getX() == x && removedMove.getY() == y);
			
			updateGameStage(move); // aktualisiere den Spielstatus
			
			redTurn = !redTurn; // der andere Spieler ist jetzt am Zug
			setCurrentPlayer(redTurn ? playerRed : playerYellow);
			
			return true;
		}
		return false;
	}
	
	public void undoMove(Move move, int rating, int position) {
		int x = move.getX(), y = move.getY();
		
		if(heights[x][y] == this.z) getValideMoves().add(position, move);
		
		heights[x][y]--; // Erhöhe die Höhe um 1
		
		int z = heights[x][y];
		
		board[x][y][z] = Token.NONE;
		
		currentStage = GameStage.GAME_NOT_ENDED;
		
		this.rating = rating;
		
		redTurn = !redTurn; // der andere Spieler ist jetzt am Zug
		setCurrentPlayer(redTurn ? playerRed : playerYellow);
	}
	
	/**
	 * Gibt zurück, ob ein Move zulässig ist
	 * @param move: der zu überprüfende Move
	 * @return	true:	der Zug ist zulässig
	 * 			false:	der Zug ist unzulässig
	 */
	public boolean isValide(Move move) {
		if(move == null) return false;
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
			if(rating == 10000) {
				currentStage = GameStage.RED_WIN;
				//System.out.println("Red wins");
			} else if(rating == -10000) {
				currentStage = GameStage.YELLOW_WIN;
				//System.out.println("Yellow wins");
			}
			return false;
		}
		return true;
	}
	
	public boolean updateRating(Move move) {
		List<List<Token>> rows = getRows(move, heights[move.getX()][move.getY()] - 1);
		Token friendlyToken = redTurn ? Token.RED : Token.YELLOW; 
		Token enemyToken = !redTurn ? Token.RED : Token.YELLOW;
		
		int sign = redTurn ? 1 : -1;
		
		int height = heights[move.getX()][move.getY()] - 1; // already updated
		
		if(height == 0) rating += sign * 2; // Züge weiter unten führen kurzfristig zu mehr Chancen
		else if(height == 1) rating += sign;
		
		for(List<Token> row : rows) {
			int tokenCounter = 0;
			int enemyTokenCounter = 0;
			
			int n = row.size();
			
			int twoRows = 0;
			int threeRows = 0;
			
			for(int i = 0; i < winningLength; i++) {
				Token currentToken = row.get(i);
				if(currentToken.equals(friendlyToken)) {
					tokenCounter++;
				} else if(currentToken.equals(enemyToken)) {
					enemyTokenCounter++;
				}
			}
			
			if(tokenCounter == winningLength) {rating = sign * 10000; break;}
			else if(tokenCounter == 1) {
				if(enemyTokenCounter == 3) threeRows++; // gegnerische 3er / 2er Reihe blockiert
				else if(enemyTokenCounter == 2) twoRows++;
			}
			else if(enemyTokenCounter == 0) {
				if(tokenCounter == 3) {
					threeRows++; // 3er / 2er Reihe erstellt
					twoRows--;
				} else if(tokenCounter == 2) twoRows++;
			}
			
			for(int i = winningLength; i < n; i++) {
				Token currentToken = row.get(i);
				if(currentToken.equals(friendlyToken)) {
					tokenCounter++;
				} else if(currentToken.equals(enemyToken)) {
					enemyTokenCounter++;
				}
				
				currentToken = row.get(i - winningLength);
				
				if(currentToken.equals(friendlyToken)) { // viert-letzter Token soll nicht mehr betrachtet werden
					tokenCounter--;
				} else if(currentToken.equals(enemyToken)) {
					enemyTokenCounter--;
				}
				
				if(tokenCounter == winningLength) {rating = sign * 10000; break;}
				else if(tokenCounter == 1) {
					if(enemyTokenCounter == 3) threeRows++; // gegnerische 3er / 2er Reihe blockiert
					else if(enemyTokenCounter == 2) twoRows++;
				}
				else if(enemyTokenCounter == 0) {
					if(tokenCounter == 3) {
						threeRows++; // 3er / 2er Reihe erstellt
						twoRows--;
					} else if(tokenCounter == 2) twoRows++;
				}
			}
			
			rating += sign * twoRows * 2;
			rating += sign * threeRows * 8;
		}
		
		if(rating >= 10000 || rating <= -10000) {
			return false; // the rating has been updated so that there is either a win or a loss
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param move
	 * @return
	 */
	public List<List<Token>> getRows(Move move, int move_z) {
		List<List<Token>> rows = new ArrayList();
		
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				for(int z = -1; z <= 1; z++) {
					if(x == 0 && y == 0 && z == 0) break; // at least one index needs to change
					
					int leftBoarder = winningLength - 1;
					int rightBoarder = winningLength - 1;
					
					int move_x = move.getX(), move_y = move.getY();
					
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
	
	public static void startGame(Game game) {
    	Player playerRed = game.getPlayerRed();
    	Player playerYellow = game.getPlayerYellow();
    	
    	playerRed.setGame(game);
    	playerYellow.setGame(game);
    	
    	// wird aufgerufen, falls man in dem UI einen Token platziert
    	Consumer<Move> placeTokenHandler = move -> {
    		if(game.isRedTurn()) {
    			playerRed.setMove(move);
    	        synchronized(playerRed) {
    	        	playerRed.notify();
    	        }
    		} else {
    			playerYellow.setMove(move);
    	        synchronized(playerYellow) {
    	        	playerYellow.notify();
    	        }
    		}
    	};
    	
    	GameEnvironment gameEnvironment = new GameEnvironment(game.x, game.y, game.z, placeTokenHandler);
        SceneController.switchScene("GAME_ENVIRONMENT", gameEnvironment.getScene()); //TODO: there might be several gameEnvironments
    	
        Thread thread = new Thread() {
        	public void run() {
        		System.out.println(game);
        		
		    	while(game.getCurrentGameStage().equals(GameStage.GAME_NOT_ENDED)) {
	    			try {
						Thread.currentThread().sleep(50); // leichte Verz�gerung andernfalls ist es möglich, dass der vorherige Zug nicht vollständig ausgeführt wurde
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    			
		    		Move move;
		    		do {
			    		if(game.isRedTurn()) {
			    			move = playerRed.getMove();
			    		} else {
			    			move = playerYellow.getMove();
			    		}
		    		} while(!game.isValide(move));
		    		game.doMove(move);
		    		
		    		final int x = move.getX();
		    		final int y = move.getY();
		    		
		    		Platform.runLater(() -> gameEnvironment.placeToken(
		    				new org.openjfx.connect_4.Grafik.Token(game.isRedTurn() ? false : true, GameEnvironment.TILE_SIZE / 2), x,
		    				y, game.heights[x][y] - 1));
		    		
		    		System.out.println(game);
		    	}
		    	if(game.getCurrentGameStage().equals(GameStage.RED_WIN)) {
		    		System.out.println(game.getPlayerRed() + " (red) wins");
		    	} else {
		    		System.out.println(game.getPlayerYellow() + " (yellow) wins");
		    	}
        	}
        };
        
        thread.start();
	}
	
	/**
	 * 
	 */
	public void presortValideMoves() {
		// jeder Move bekommt als Rating die Anzahl der M�glichen durch ihn verursachten 4er Reihen
		for(int i = 0; i < valideMoves.size(); i++) {
			Move move = valideMoves.get(i);
			List<List<Token>> rows = getRows(move, 0);
			int rating = 0;
			for(int j = 0; j < rows.size(); j++) {
				List<Token> row = rows.get(j);
				rating += row.size() - winningLength + 1; // Anzahl möglicher vierer Reihen
			}
			move.setRating(rating);
		}
		
		valideMoves.sort((move1, move2) -> (move1.getRating() < move2.getRating()) ? 1 : -1); // sortiere moves nach rating absteigend
	}
	
	public List<int[][][]> getWinningTokens() {
		List<int[][][]> tokens = new ArrayList();
		
		return tokens;
	}
	
	@Override
	public String toString() {
		String toString = (isRedTurn() ? "Rot" : "Gelb") + " ist am Zug\n";
		toString += "Rating: " + rating + "\n";
		
        for(int i = 0; i < z; i++) {
            for(int j = 0; j < x; j++) {
                for(int k = 0; k < y; k++) {
                    toString += " " + board[j][k][z - i - 1] + " ";
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

	public Player getPlayerRed() {
		return playerRed;
	}

	public Player getPlayerYellow() {
		return playerYellow;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isRedTurn() {
		return redTurn;
	}

	public List<Move> getValideMoves() {
		return valideMoves;
	}
}
