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
	
	private long timeOut = 1000;

	@Override
	public Move getMove() {
		startTime = System.currentTimeMillis();
		currentDepth = 2;
		
		try {
			while(true) {
				if(game.isRedTurn()) {
					System.out.println("Predicted Rating: " + maximize(currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE));
				} else {
					System.out.println("Predicted Rating: " + minimize(currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE));
				}
				currentDepth++;
				
			}
		} catch(TimeOutException exc) {
			//exc.printStackTrace();
			return bestMove;
		}
		
		return bestMove;
	}
	
	private int maximize(int depth, int alpha, int beta) throws TimeOutException {
		int maxEval = Integer.MIN_VALUE;
		int rating = game.getRating();
		
		if(!game.getCurrentGameStage().equals(GameStage.GAME_NOT_ENDED) || depth == 0)
			return rating;
		
		for(Move move : game.getValideMoves()) {
			if(System.currentTimeMillis() > startTime + 10000) continue;
			
			game.doMove(move);
			int eval = minimize(depth - 1, alpha, beta);
			game.undoMove(move, rating);
			
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
		
		for(Move move : game.getValideMoves()) {
			game.doMove(move);
			int eval = maximize(depth - 1, alpha, beta);
			game.undoMove(move, rating);
			
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
	
	@Override
	public String toString() {
		return "Bot";
	}	

}
