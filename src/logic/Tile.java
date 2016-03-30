package logic;

import java.awt.Point;
import java.util.ArrayList;

public class Tile {
	private ArrayList<Percepts> percepts = new ArrayList<Percepts>();
	private int X;
	private int Y;
	private Point point;
	
	public Tile(int xPosition, int yPosition, ArrayList<Percepts> percepts) {
		point = new Point(xPosition, yPosition);
		this.percepts = percepts;
	}
	
	public Tile(int x, int y){
		point = new Point(x,y);
	}
	
	public Point getLocation(){
		return point.getLocation();
	}

	public Percepts[] getPercepts() {
		return (Percepts[]) percepts.toArray();
	}

	public void setPercepts(ArrayList<Percepts> percepts) {
		this.percepts = percepts;
	}


	

}
