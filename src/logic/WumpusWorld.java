package logic;

import java.awt.Point;

public class WumpusWorld implements AgentActions{

	private Tile[][] world;
	private int playerX;
	private int playerY;
	private char playerOR;
	
	//A hard coded map 4x4 map
	public WumpusWorld(){
		world = new Tile[4][4];
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				world[i][j] = new Tile(i,j);
			}
		}
		playerX = 0;
		playerY = 0;
		playerOR = 'n';
	}
	
	
	public Tile getTile(int x, int y){
		return world[x][y];
	}
	
	public Tile[][] getWorld(){
		return world;
	}
	
	public boolean move(int direction){
		switch (direction){
		//move up
		case 0:
			if(playerOR == 'n'){
				//actually move
				playerY++;
				return true;
			}else{
				//set orientation
				playerOR = 'n';
				return false;
			}
		//move right	
		case 1:
			if(playerOR == 'e'){
				playerX++;
				return true;
			}else{
				playerOR = 'e';
				return false;
			}
			// move down
		case 2:
			if(playerOR == 's'){
				playerY--;
				return true;
			}else{
				playerOR = 's';
				return false;
			}
			// move left
		case 3:
			if(playerOR == 'w'){
				playerX--;
				return true;
			}else{
				playerOR = 'w';
				return false;
			}
		}
		//should never get to this point
		return false;
	}
	
	
	public Point getPlayerPosition(){
		return new Point(playerX,playerY);
	}
	
	public int getPlayerX(){
		return playerX;
	}
	
	public int getPlayerY(){
		return playerY;
	}
	
	public char getPlayerOrientation(){
		return playerOR;
	}
	
	
}
