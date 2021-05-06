package main.tictactoe.server;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JTextField;

public class Game {

	CopyOnWriteArrayList<Game> games = new CopyOnWriteArrayList<Game>();

	private static final int NUMBER_ROW_COLUMNS = 3;

	
	private final String[][] fields;

	private Player player1;

	private Player player2;

	public Game(JTextField turn) {
		fields = new String[NUMBER_ROW_COLUMNS][NUMBER_ROW_COLUMNS];
		player1 = null;
		player2 = null;
	}

	public void addFirstPlayer(Player player) {
		player1 = player;
	}

	public void addSecondPlayer(Player player) {
		player2 = player;
	}

	// Not sure whether you need this
	public void removeFirstPlayer() {
		player1 = null;
	}

	// Not sure whether you need this
	public void removeSecondPlayer() {
		player2 = null;
	}

	// Not sure whether you need this
	public Player getFirstPlayer() {
		return player1;
	}

	// Not sure whether you need this
	public Player getSecondPlayer() {
		return player2;
	}
	

	public void addMoveByPlayer(String player, int row, int column) {
		if (player.equals(player1)) {
			// The first player is always X
			fields[row][column] = "X";
		} else {
			// The second player is always O
			fields[row][column] = "O";
		}
	}


	public Player checkHasPlayerWon() {
		// TODO: First check vertically
		for (int row = 0; row < NUMBER_ROW_COLUMNS; row++) {
			// Save the first mark of the row
			String firstMark = fields[row][0];
			if (firstMark.equals("X") || firstMark.equals("O")) {
				// If the first mark of the row is not set, then we can ignore the row, because a player
				// can only win if every mark of a row is set to either X or O
				boolean playerHasWon = true;
				for (int column = 1; column < NUMBER_ROW_COLUMNS; column++) {
					// Now check whether every mark of this row is equal to the first mark
					if (!firstMark.equals(fields[row][column])) {
						// If one mark is not equal, we know that the player has not won in this row
						playerHasWon = false;
					}
				}
				if (playerHasWon) {
					if (firstMark.equals("X")) {
						return player1;
					} else if (firstMark.equals("O")) {
						return player2;
					}
				}
			}
		}
		// TODO: Then check horizontally
		for (int column = 0; column < NUMBER_ROW_COLUMNS; column++) {
			// Save the first mark of the row
			String firstMark = fields[column][0];
			if (firstMark.equals("X") || firstMark.equals("O")) {
				// If the first mark of the row is not set, then we can ignore the row, because a player
				// can only win if every mark of a row is set to either X or O
				boolean playerHasWon = true;
				for (int row = 1; row < NUMBER_ROW_COLUMNS; row++) {
					// Now check whether every mark of this row is equal to the first mark
					if (!firstMark.equals(fields[column][row])) {
						// If one mark is not equal, we know that the player has not won in this row
						playerHasWon = false;
					}
				}
				if (playerHasWon) {
					if (firstMark.equals("X")) {
						return player1;
					} else if (firstMark.equals("O")) {
						return player2;
					}
				}
			}
		}
		// Lastly check diagonally.

		// First check from top left to bottom right.

		// Save the mark and compare it to all other marks.
		// If this mark is not equal to all other marks, then no player has won.
		String markFirstRowAndColumn = fields[0][0];

		if (markFirstRowAndColumn.equals("X") || markFirstRowAndColumn.equals("O")) {
			// If the first mark is not set, then we can ignore it, because a player can only win if every mark is set.

			// Boolean flag to keep track whether a mark was found which is not equal to the first mark.
			boolean playerHasWon = true;
			for (int rowAndColumn = 1; rowAndColumn < NUMBER_ROW_COLUMNS; rowAndColumn++) {
				// Now check whether the other marks are equal to the first mark.
				if (!markFirstRowAndColumn.equals(fields[rowAndColumn][rowAndColumn])) {
					// If one mark is not equal, we know that the player has not won.
					playerHasWon = false;
					// Immediately stop searching because if one mark is not equal to the first mark, a player
					// cannot win.
					break;
				}
			}

			if (playerHasWon) {
				if (markFirstRowAndColumn.equals("X")) {
					return player1;
				} else if (markFirstRowAndColumn.equals("O")) {
					return player2;
				}
			}
		}

		// Now check from top right to bottom left

		// Save the mark and compare it to all other marks.
		// If this mark is not equal to all other marks, then no player has won.

		int lastColumn = NUMBER_ROW_COLUMNS - 1;
		markFirstRowAndColumn = fields[0][lastColumn];

		if (markFirstRowAndColumn.equals("X") || markFirstRowAndColumn.equals("O")) {
			// If the first mark is not set, then we can ignore it, because a player can only win if every mark is set.

			// Boolean flag to keep track whether a mark was found which is not equal to the first mark.
			boolean playerHasWon = true;
			for (int row = 1; row < NUMBER_ROW_COLUMNS; row++) {
				// Now check whether the other marks are equal to the first mark.
				if (!markFirstRowAndColumn.equals(fields[row][lastColumn - row])) {
					// If one mark is not equal, we know that the player has not won.
					playerHasWon = false;
					// Immediately stop searching because if one mark is not equal to the first mark, a player
					// cannot win.
					break;
				}
			}

			if (playerHasWon) {
				if (markFirstRowAndColumn.equals("X")) {
					return player1;
				} else if (markFirstRowAndColumn.equals("O")) {
					return player2;
				}
			}
		}

		// If this point is reached, no player has won and the method returns null to indicate that the game is not
		// over yet.
		//    	 ___ ___ ___
		//    	|_X_|___|___|   NUMBER_ROW_COLUMNS = 0
		//    	|___|_X_|___|	NUMBER_ROW_COLUMNS = 1
		//    	|___|___|_X_|	NUMBER_ROW_COLUMNS = 2


		return null;
	}
}



