package logic;

import java.awt.Point;
import java.util.ArrayList;

public class Tile {
	private ArrayList<Percepts> percepts = new ArrayList<Percepts>();
	private boolean isVisible;
	private int X;
	private int Y;
	private Objectives obj;
	
	public Tile(int xPosition, int yPosition, ArrayList<Percepts> percepts, Objectives obj) {
		this.X = xPosition;
		this.Y = yPosition;
		this.percepts = percepts;
		this.obj = obj;
		isVisible = false;
	}
	
	public Tile(int x, int y){
		this.X = x;
		this.Y = y;
	}
	
	public Objectives getObjective(){
		return obj;
	}
	
	public void setObjective(Objectives obj){
		this.obj = obj;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible() {
		isVisible = true;
	}
	
	public Point getLocation(){
		return new Point(X,Y);
	}
	
	public int getY(){
		return Y;
	}
	
	public int getX(){
		return X;
	}

	public Percepts[] getPercepts() {
		Percepts[] toReturn = new Percepts[percepts.size()];
		toReturn = percepts.toArray(toReturn);
		return toReturn; 
	}

	public void addPrecept(Percepts p){
		percepts.add(p);
	}
	
	public void setPercepts(ArrayList<Percepts> percepts) {
		this.percepts = percepts;
	}

}
