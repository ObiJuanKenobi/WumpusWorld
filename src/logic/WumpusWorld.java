package logic;

import java.awt.Point;

public class WumpusWorld implements AgentActions{

	private Tile[][] world;
	private Player p;
	
	
	//A hard coded map
	public WumpusWorld(){
		
	}
	
	//handles orientation
	public void move(int direction){
		
	}
	
	
	public Point getPlayerPosition(){
		return p.getPosition();
	}
	
	public char getPlayerOrientation(){
		return p.getOrientation();
	}
	
	
}
