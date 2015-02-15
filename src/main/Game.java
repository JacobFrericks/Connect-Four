package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Game {

	/** The board to play on */
	char[][] board;
	/**
	 * The char if there is no piece. NOTE: '\u0000' turns out to be the similar
	 * to null for a char
	 */
	public final static char nullChar = '\u0000';
	/** The color of the AI */
	public final static char aiColor = 'b';
	/** The color of player 1 */
	public final static char player1Color = 'r';
	/** The color of player 2 */
	public final static char player2Color = 'b';

	public static void main(String[] args) throws InterruptedException,
			IOException {
		Game game = new Game();
		game.board = new char[6][7];

		Player player1 = new Player(1);
		Player player2 = new Player(2);

		// get info from the player (multiplayer/difficulty/etc)
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.println("Multiplayer? (y/n)");
		String multiPlayer = bufferRead.readLine();
		multiPlayer = multiPlayer.toLowerCase();

		while (multiPlayer != null && multiPlayer.charAt(0) != 'y'
				&& multiPlayer.charAt(0) != 'n') {
			System.out.println("Error, please try again. Single player? (y/n)");
			multiPlayer = bufferRead.readLine();
		}

		String difficulty = null;
		if (multiPlayer.charAt(0) == 'n') {
			System.out
					.println("What difficulty would you like? (SuperEasy/Easy)");
			difficulty = bufferRead.readLine();
			difficulty = difficulty.toLowerCase();
		}

		while (difficulty != null && difficulty.charAt(0) != 's'
				&& difficulty.charAt(0) != 'e') {
			System.out
					.println("Error, please try again. What difficulty would you like? (SuperEasy/Easy)");
			difficulty = bufferRead.readLine();
		}

		game.printGameBoard();

		String index;
		while (true) {
			// Player 1's turn
			System.out
					.println("Player 1: What column number would you like to put your piece?");
			index = bufferRead.readLine();

			Game.sinkChar(game.board, Integer.parseInt(index) - 1,
					player1.getPlayerColor());
			game.printGameBoard();

			if (isGameOver(game.board, Game.player1Color)
					|| isGameOver(game.board, Game.player2Color)
					|| game.isCatsGame(game.board)) {
				System.out.println("GAME OVER");
				break;
			}

			if (multiPlayer.startsWith("y")) {
				System.out
						.println("Player 2: What column number would you like to put your piece?");
				index = bufferRead.readLine();

				Game.sinkChar(game.board, Integer.parseInt(index) - 1,
						player2.getPlayerColor());
			} else {
				System.out.println("My turn!");
				game.AIsMove(game.board, difficulty.charAt(0));
			}

			game.printGameBoard();

			if (isGameOver(game.board, Game.player1Color)
					|| isGameOver(game.board, Game.player2Color)
					|| game.isCatsGame(game.board)) {
				System.out.println("GAME OVER");
				break;
			}
		}
	}

	/**
	 * clears the board for another round
	 */
	public void clearBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = nullChar;
			}
		}
	}

	/**
	 * Determines if the game is over
	 * 
	 * @param board
	 * @param playerColor
	 * @return true if it is a cats game, or if there are 4 of the same piece in
	 *         a row.
	 */
	public static boolean isGameOver(char[][] board, char playerColor) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				// checks for horizontal 4-in-a-row starting at the point
				if (j < 4 && board[i][j] == playerColor
						&& board[i][j] == board[i][j + 1]
						&& board[i][j + 1] == board[i][j + 2]
						&& board[i][j + 2] == board[i][j + 3]) {
					return true;
				}
				// checks for vertical 4-in-a-row starting at the point
				else if (i < 3 && board[i][j] == playerColor
						&& board[i][j] == board[i + 1][j]
						&& board[i + 1][j] == board[i + 2][j]
						&& board[i + 2][j] == board[i + 3][j]) {
					return true;
				}
				// checks for diagonal 4-in-a-row starting at the point
				// checks for diagonals going from top left to bottom right
				if (i < 3 && j <= 3 && board[i][j] == playerColor
						&& board[i][j] == board[i + 1][j + 1]
						&& board[i + 1][j + 1] == board[i + 2][j + 2]
						&& board[i + 2][j + 2] == board[i + 3][j + 3]) {
					return true;
				}
				// checks for diagonals going from top right to bottom left
				if (i < 3 && j >= 3 && board[i][j] == playerColor
						&& board[i][j] == board[i + 1][j - 1]
						&& board[i + 1][j - 1] == board[i + 2][j - 2]
						&& board[i + 2][j - 2] == board[i + 3][j - 3]) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * * if there is a piece in every slot on the top row, then it is a cats
	 * game
	 * 
	 * @param board
	 * @return true if it is a cats game
	 */
	public boolean isCatsGame(char board[][]) {
		for (int j = 0; j < board[0].length; j++) {
			if (board[0][j] == nullChar) {
				return false;
			}
		}
		return true;
	}

	/**
	 * prints out the game board
	 */
	public void printGameBoard() {
		// System.out.println("------------------------");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print("|" + board[i][j] + "|");
			}
			System.out.println("");
		}
		System.out.println("------------------------");
		// print row numbers
		for (int j = 1; j <= board[0].length; j++) {
			System.out.print("|" + j + "|");
		}
		System.out.println("");
	}

	/**
	 * puts the char at the column index, in the lowest possible row
	 * 
	 * @param index
	 *            column index
	 * @param color
	 *            the players color ('r' or 'b')
	 * @return the new board if successful, null if not
	 */
	public static char[][] sinkChar(char[][] board, int index, char color) {
		char[][] boardToInsert = board.clone();
		if (index <= boardToInsert[0].length
				|| boardToInsert[0][index] == nullChar) {
			for (int i = 1; i < boardToInsert.length; i++) {
				// if we hit another piece before the bottom
				if (boardToInsert[i][index] != nullChar) {
					boardToInsert[i - 1][index] = color;
					return boardToInsert;
				}
				// if we are at the bottom of the board
				if (i == boardToInsert.length - 1) {
					boardToInsert[i][index] = color;
					return boardToInsert;
				}
			}
		}
		return null;
	}

	/**
	 * based on the difficulty, the AI chooses where to go
	 * 
	 * @param board
	 * @param difficulty
	 */
	private void AIsMove(char[][] board, char difficulty) {
		// randomly selects a row
		if (difficulty == 's') {
			Random rand = new Random();
			char[][] newBoard = Game.sinkChar(board, rand.nextInt(7),
					Game.aiColor);
			while (newBoard == null) {
				newBoard = Game.sinkChar(board, rand.nextInt(7), Game.aiColor);
			}
			// based on the minimax value, choose a place to go
		} else if (difficulty == 'e') {
			ArrayList<Move> moves = new ArrayList<Move>(7);
			for (int i = 0; i < 7; i++) {
				Move moveToAdd = new Move(board, i);
				moveToAdd.setMiniMaxValue(miniMax(moveToAdd, 3));
				moves.add(moveToAdd);
			}

			System.out.println("");
			for (int i = 0; i < moves.size(); i++) {
				Move move = moves.get(i);
				if (board[0][move.getColumn()] != nullChar) {
					moves.remove(i);
					i--;
				}
			}

			Random rand = new Random();
			Move bestMove = moves.get(rand.nextInt(moves.size()));
			for (Move move : moves) {
				if (move.getMiniMaxValue() > bestMove.getMiniMaxValue()) {
					bestMove = move;
				}
			}
			Game.sinkChar(board, bestMove.getColumn(), Game.aiColor);
		}
	}

	/**
	 * 
	 * @param move
	 *            the move
	 * @param depth
	 *            the depth
	 * @return the total heuristic at this move based on minimax
	 */
	private int miniMax(Move move, int depth) {
		if (isGameOver(move.getNewBoard(), Game.aiColor)) {
			return Integer.MAX_VALUE;
		}
		
		if (depth == 0) {
			return move.getHeuristic();
		}

		int value = Integer.MAX_VALUE;

		for (int i = 0; i < 7; i++) {
			value = Math.min(value,
					-1 * miniMax(new Move(move.getNewBoard(), i), depth - 1));
		}

		return value;
	}
}