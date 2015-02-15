package main;

public class Move {
	private char[][] newBoard;
	private char[][] oldBoard;
	private int columnMoved;
	private int rowMoved = -1;
	private int heuristic;
	private int miniMaxValue;
	private final int threeInARow = 32;
	private final int twoInARow = 4;
	private final int oneInARow = 1;

	public Move(char[][] board, int column) {
		this.setColumn(column);

		// sets oldBoard
		oldBoard = new char[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				oldBoard[i][j] = board[i][j];
			}
		}

		// copies the board to the new board and inserts an AI color in the
		// given column
		newBoard = new char[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				newBoard[i][j] = board[i][j];
			}
		}

		newBoard = Game.sinkChar(newBoard, column, Game.aiColor);

		// find the row that was moved to
		for (int j = 0; j < newBoard.length; j++) {
			if (!(newBoard[j][columnMoved] + "").equals(Game.nullChar + "")) {
				rowMoved++;
			}
		}
		heuristic = getHeuristic();
	}

	public int getColumn() {
		return columnMoved;
	}

	public void setColumn(int column) {
		this.columnMoved = column;
	}

	public int getHeuristic() {
		int heuristic = 0;
		if (blockedAWin(newBoard)) {
			return Integer.MAX_VALUE;
		}

		// Column Check
		int numberOfTopPieces = columnCheck(columnMoved);
		if (numberOfTopPieces == 3) {
			heuristic += threeInARow;
		} else if (numberOfTopPieces == 2) {
			heuristic += twoInARow;
		}

		// Row Check
		int numberInRow = rowCheck(rowMoved);
		if (numberInRow == 3) {
			heuristic += threeInARow;
		} else if (numberInRow == 2) {
			heuristic += twoInARow;
		}

		// Diagonal Check
		int numberInDiagonal = diagonalCheck(columnMoved, rowMoved);
		if (numberInDiagonal == 3) {
			heuristic += threeInARow;
		} else if (numberInDiagonal == 2) {
			heuristic += twoInARow;
		}

		return heuristic;
	}

	public char[][] getNewBoard() {
		return newBoard;
	}

	public void setNewBoard(char[][] newBoard) {
		this.newBoard = newBoard;
	}

	public int getMiniMaxValue() {
		return miniMaxValue;
	}

	public void setMiniMaxValue(int miniMaxValue) {
		this.miniMaxValue = miniMaxValue;
	}

	public String toString() {
		return miniMaxValue + "";
	}

	/**
	 * gives the number of AI color pieces at the top of the given column
	 * 
	 * @param column
	 * @return the number of AI color pieces at the top of the given column
	 */
	public int columnCheck(int column) {
		int counter = 0;
		for (int i = 0; i < newBoard.length; i++) {
			// no possibility of 4 in a column if the 4th one down or above is
			// the other
			// color
			if (i <= 3 && newBoard[i][column] == Game.player1Color) {
				return 0;
			}
			// increments counter until a player1's color is hit
			if (newBoard[i][column] == Game.aiColor) {
				counter++;
			} else if (newBoard[i][column] == Game.player1Color) {
				break;
			}
		}
		return translateToHeuristic(counter);
	}

	/**
	 * gives the number of ai color pieces that was created by the given move
	 * 
	 * @param column
	 * @return the number of AI color pieces at the top of the given column
	 */
	public int rowCheck(int rowToCheck) {
		int tempCounter = 0;
		int heuristic = 0;
		int column = columnMoved;
		int row = rowToCheck;
		int constraint = 1;

		// ???b
		while (columnMoved >= 3 && constraint <= 4) {
			if (newBoard[row][column] == Game.aiColor) {
				tempCounter++;
			} else if (newBoard[row][column] == Game.player1Color) {
				break;
			}
			column--;
			constraint++;
		}

		heuristic += translateToHeuristic(tempCounter);

		// ??b?
		// NOTE: column is columnMoved-1 on the first because we don't want to
		// count it twice
		column = columnMoved - 1;
		row = rowToCheck;
		constraint = 1;
		tempCounter = 0;
		while (columnMoved - 1 >= 1 && constraint <= 2) {
			if (newBoard[row][column] == Game.aiColor) {
				tempCounter++;
			} else if (newBoard[row][column] == Game.player1Color) {
				// don't count the section if it contains a black square
				tempCounter = 0;
				break;
			}
			column--;
			constraint++;
		}
		column = columnMoved;
		row = rowToCheck;
		constraint = 1;
		while (columnMoved <= 4 && constraint <= 2) {
			if (newBoard[row][column] == Game.aiColor) {
				tempCounter++;
			} else if (newBoard[row][column] == Game.player1Color) {
				// don't count the section if it contains a black square
				tempCounter = 0;
				break;
			}
			column++;
			constraint++;
		}

		heuristic += translateToHeuristic(tempCounter);

		// ?b??
		column = columnMoved;
		row = rowToCheck;
		constraint = 1;
		tempCounter = 0;
		while (columnMoved >= 1 && constraint <= 2) {
			if (newBoard[row][column] == Game.aiColor) {
				tempCounter++;
			} else if (newBoard[row][column] == Game.player1Color) {
				// don't count the section if it contains a black square
				tempCounter = 0;
				break;
			}
			column--;
			constraint++;
		}
		// NOTE: column is columnMoved+1 on the first because we don't want to
		// count it twice
		column = columnMoved + 1;
		row = rowToCheck;
		constraint = 1;
		while (columnMoved + 1 <= 5 && constraint <= 2) {
			if (newBoard[row][column] == Game.aiColor) {
				tempCounter++;
			} else if (newBoard[row][column] == Game.player1Color) {
				// don't count the section if it contains a black square
				tempCounter = 0;
				break;
			}
			column++;
			constraint++;
		}

		heuristic += translateToHeuristic(tempCounter);

		// b???
		column = columnMoved;
		row = rowToCheck;
		constraint = 1;
		tempCounter = 0;
		while (columnMoved <= 3 && constraint <= 4) {
			if (newBoard[row][column] == Game.aiColor) {
				tempCounter++;
			} else if (newBoard[row][column] == Game.player1Color) {
				// don't count the section if it contains a black square
				tempCounter = 0;
				break;
			}
			column++;
			constraint++;
		}

		heuristic += translateToHeuristic(tempCounter);
		return heuristic;
	}

	public int diagonalCheck(int columnNumber, int rowNumber) {
		// up left
		int column = columnNumber;
		int row = rowNumber;
		int counter = 0;

		if (row >= 3 && column >= 3) {
			for (int i = 0; i > 4; i++) {
				if (newBoard[row][column] == Game.aiColor) {
					counter++;
				} else if (newBoard[row][column] == Game.player1Color) {
					counter = 0;
					break;
				}
				column--;
				row--;
			}
			// down right
		} else if (row >= 3 && column <= 3) {
			for (int i = 0; i > 4; i++) {
				if (newBoard[row][column] == Game.aiColor) {
					counter++;
				} else if (newBoard[row][column] == Game.player1Color) {
					counter = 0;
					break;
				}
				column--;
				row--;
			}
		}
		return translateToHeuristic(counter);
	}

	private int translateToHeuristic(int translatee) {
		if (translatee == 1) {
			return oneInARow;
		} else if (translatee == 2) {
			return twoInARow;
		} else if (translatee == 3) {
			return threeInARow;
		} else
			return 0;
	}

	/**
	 * Determines if the game is over
	 * 
	 * @param board
	 * @return true if it is a cats game, or if there are 4 of the same piece in
	 *         a row.
	 */
	private boolean player1CanWinNextTurn(char[][] board) {
		char playerColor = Game.player1Color;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				// checks for horizontal 3-in-a-row starting at the point
				// rrrx
				if (j < 4 && board[i][j] == playerColor
						&& board[i][j] == board[i][j + 1]
						&& board[i][j + 1] == board[i][j + 2]
						&& board[i][j + 3] == Game.nullChar) {
					return true;
				}

				// xrrr
				if (j < 4 && board[i][j] == Game.nullChar
						&& board[i][j + 1] == board[i][j + 2]
						&& board[i][j + 2] == board[i][j + 3]
						&& board[i][j + 3] == Game.player1Color) {
					return true;
				}

				// checks for vertical 3-in-a-row starting at the point
				else if (i < 3 && board[i][j] == Game.nullChar
						&& board[i + 1][j] == board[i + 2][j]
						&& board[i + 2][j] == board[i + 3][j]
						&& board[i + 3][j] == Game.player1Color) {
					return true;
				} // checks for diagonal 4-in-a-row starting at the point
					// checks for diagonals going from top left to bottom right
				if (i < 3 && j <= 3 && board[i][j] == playerColor
						&& board[i][j] == board[i + 1][j + 1]
						&& board[i + 1][j + 1] == board[i + 2][j + 2]
						&& board[i + 3][j + 3] == Game.nullChar) {
					return true;
				}

				if (i < 3 && j <= 3 && board[i][j] == Game.nullChar
						&& board[i + 1][j + 1] == board[i + 2][j + 2]
						&& board[i + 2][j + 2] == board[i + 3][j + 3]
						&& board[i + 3][j + 3] == Game.player1Color) {
					return true;
				}
				// checks for diagonals going from top right to bottom left
				if (i < 3 && j >= 3 && board[i][j] == playerColor
						&& board[i][j] == board[i + 1][j - 1]
						&& board[i + 1][j - 1] == board[i + 2][j - 2]
						&& board[i + 3][j - 3] == Game.nullChar) {
					return true;
				}

				if (i < 3 && j >= 3 && board[i][j] == Game.nullChar
						&& board[i + 1][j - 1] == board[i + 2][j - 2]
						&& board[i + 2][j - 2] == board[i + 3][j - 3]
						&& board[i + 3][j - 3] == Game.player1Color) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Determines if the game is over
	 * 
	 * @param board
	 * @return true if it is a cats game, or if there are 4 of the same piece in
	 *         a row.
	 */
	private boolean blockedAWin(char[][] board) {
		int column = columnMoved;
		int row = rowMoved;

		// horizontal
		// rrrb
		if (column > 2 && board[row][column - 1] == Game.player1Color
				&& board[row][column - 1] == board[row][column - 2]
				&& board[row][column - 2] == board[row][column - 3]) {
			return true;
		}

		// brrr
		if (column < 4 && board[row][column + 1] == Game.player1Color
				&& board[row][column + 1] == board[row][column + 2]
				&& board[row][column + 2] == board[row][column + 3]) {
			return true;
		}

		// vertical
		if (row < 3 && board[row + 1][column] == Game.player1Color
				&& board[row + 1][column] == board[row + 2][column]
				&& board[row + 2][column] == board[row + 3][column]) {
			return true;
		}

		// checks for diagonal 4-in-a-row starting at the point
		// checks for diagonals going from top left to bottom right
		if (row < 3 && column <= 3
				&& board[row + 1][column + 1] == board[row + 2][column + 2]
				&& board[row + 2][column + 2] == board[row + 3][column + 3]
				&& board[row + 3][column + 3] == Game.player1Color) {
			return true;
		}

		if (row >= 3 && column >= 3
				&& board[row - 1][column - 1] == board[row - 2][column - 2]
				&& board[row - 2][column - 2] == board[row - 3][column - 3]
				&& board[row - 3][column - 3] == Game.player1Color) {
			return true;
		}
		// checks for diagonals going from top right to bottom left
		if (row < 3 && column >= 3
				&& board[row + 1][column - 1] == board[row + 2][column - 2]
				&& board[row + 2][column - 2] == board[row + 3][column - 3]
				&& board[row + 3][column - 3] == Game.player1Color) {
			return true;
		}

		if (row > 2 && column < 4
				&& board[row - 1][column + 1] == board[row - 2][column + 2]
				&& board[row - 2][column + 2] == board[row - 3][column + 3]
				&& board[row - 3][column + 3] == Game.player1Color) {
			return true;
		}
		return false;
	}
}