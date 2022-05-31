package org.openjfx.connect_4.Logik;

import java.util.Scanner;

public class ConsolePlayer extends Player {
	public ConsolePlayer(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	public ConsolePlayer() {}

	private final Scanner SCAN = new Scanner(System.in);
	
	/*
	 * 
	 */
	
	@Override
	public Move getMove() {
		Move move;
		
		do {
			int x = getConsoleInteger(0, super.x - 1);
			int y = getConsoleInteger(0, super.y - 1);
			move = new Move(x, y);
		} while(!game.isValide(move));
		
		return move;
	}
	
	/**
	 * 
	 * @param min : untere Schranke
	 * @param max : obere Schranke
	 * @return : über Konsole eingegebene ganze Zahl innerhalb der Schranken
	 */
	public int getConsoleInteger(int min, int max) {
		int number;
		
		do {
			number = SCAN.nextInt();
		} while(number < min && number > max);
		
		return number;
	}
}
