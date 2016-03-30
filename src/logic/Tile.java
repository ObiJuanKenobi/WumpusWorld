package logic;

import java.util.ArrayList;

public class Tile {
	private ArrayList<Percepts> percepts = new ArrayList<Percepts>();
	private int X;
	private int Y;
	private Player p;
	public Tile(int xPostion, int yPosition, ArrayList<Percepts> percepts) {
		X = xPostion;
		Y = yPosition;
		this.percepts = percepts;
	}

	public Percepts[] getPercepts() {
		return (Percepts[]) percepts.toArray();
	}

	public void setPercepts(ArrayList<Percepts> percepts) {
		this.percepts = percepts;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
	

}
