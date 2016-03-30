package logic;

import java.awt.Point;

public class Player {
	private Point p;
	private char orientation;
	private boolean hasGold = false;
	
	public Player(char orientation){
		p = new Point();
		this.orientation = orientation;
	}
	
	public Player(int x, int y, char orientation){
		p = new Point(x,y);
		this.orientation = orientation;
	}
	
	public Point getPosition(){
		return p.getLocation();
	}
	
	public void setPosition(int x, int y){
		p.setLocation(x, y);
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
	
	public boolean hasGold(){
		return hasGold;
	}
}
