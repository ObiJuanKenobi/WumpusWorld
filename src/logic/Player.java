package logic;

public class Player {
	private int xPosition;
	private int yPosition;
	private char orientation;
	private boolean hasGold = false;
	
	public Player(int x, int y, char orientation){
		xPosition = x;
		yPosition = y;
		this.orientation = orientation;
	}

	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public char getOrientation() {
		return orientation;
	}

	public void setOrientation(char orientation) {
		this.orientation = orientation;
	}
	
	public void setHasGold(boolean hasGold){
		this.hasGold = hasGold;
	}
}
