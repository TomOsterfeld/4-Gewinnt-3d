package org.openjfx.connect_4.Logik;

import java.util.List;

import org.openjfx.connect_4.Logik.Game.GameStage;

/**
 * 
 * @author Tom
 *
 */
public class BotPlayer extends Player {
	private int currentDepth;
	private boolean safeWin;
	private long startTime;
	private Move bestMove;
	List<Move> valideMoves;
	
	private long timeOut = 10000;

	@Override
	public Move getMove() {
		startTime = System.currentTimeMillis();
		currentDepth = 1;
		
		valideMoves = game.getValideMoves();
		
		Move bestMove = valideMoves.get(0);
		
		while(!timeOut()) {
			if(this.bestMove != null) { // der vorherige beste Move wird lokal gespeichert und an den Beginn der Liste gesetzt
				bestMove = this.bestMove;
				game.getValideMoves().remove(bestMove);
				game.getValideMoves().add(0, bestMove);
			}
			
			if(safeWin) break;
			
			int rating;
			
			if(game.isRedTurn()) {
				rating = maximize(currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
				if(rating == 10000) safeWin = true;
				else if(rating == -10000) break; // garantierte Niederlage
			} else {
				rating = minimize(currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
				if(rating == -10000) safeWin = true;
				else if(rating == 10000) break;
			}
			
			System.out.println("Predicted Rating: " + rating
					+ " for Depth " + currentDepth + (timeOut()? " (timeout)" : "")
					+ " after " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
			
			currentDepth++;
		}
		
		this.bestMove = null;
		safeWin = false;
		return bestMove;
	}
	
	private int maximize(int depth, int alpha, int beta) {
		int maxEval = Integer.MIN_VALUE;
		int rating = game.getRating();
		
		if(!game.getCurrentGameStage().equals(GameStage.GAME_NOT_ENDED) || depth == 0)
			return rating;
		
		for(int i = 0; i < game.getValideMoves().size(); i++) {
			if(timeOut()) break;
			
			Move move = game.getValideMoves().get(i);
			
			game.doMove(move);
			int eval = minimize(depth - 1, alpha, beta);
			game.undoMove(move, rating, i);
			
			if(eval > maxEval) {
				maxEval = eval;
				if(depth == currentDepth) {
					bestMove = move;
				}
			}
			
			alpha = Math.max(alpha, eval);
            if (beta <= alpha) break; // alpha-beta pruning
		}
		
		return maxEval;
	}
	
	private int minimize(int depth, int alpha, int beta) {
		int minEval = Integer.MAX_VALUE;
		int rating = game.getRating();
		
		if(!game.getCurrentGameStage().equals(GameStage.GAME_NOT_ENDED) || depth == 0)
			return rating;
		
		for(int i = 0; i < game.getValideMoves().size(); i++) {
			if(timeOut()) break;
		
			Move move = game.getValideMoves().get(i);
			
			game.doMove(move);
			int eval = maximize(depth - 1, alpha, beta);
			game.undoMove(move, rating, i);
			
			if(eval < minEval) {
				minEval = eval;
				if(depth == currentDepth) {
					bestMove = move;
				}
			}
			
			beta = Math.min(beta, eval);
            if (beta <= alpha) break;
		}
		
		return minEval;
	}
	
	private boolean timeOut() {
		return System.currentTimeMillis() > startTime + timeOut;
	}
	
	@Override
	public String toString() {
		return "Bot";
	}	

}
