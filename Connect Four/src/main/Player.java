package main;

public class Player {

	private int playerNum;
	private char playerColor;

	public Player(int playerNum) {
		this.playerNum = (playerNum + 1) % 2;
		if (this.playerNum == 0) {
			playerColor = Game.player1Color;
		} else {
			playerColor = Game.player2Color;
		}
	}

	public char getPlayerColor() {
		return playerColor;
	}

	public int getPlayerNumber() {
		return playerNum;
	}
}
