package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
/**
 * @author Paul
 * @author Juan
 * 
 * TODO: Get Surrounding Percepts based on where the player is
 * TODO: Handle Player & Objectives interaction 
 * TODO: Handle Game Over
 * TODO: Handle Game Won
 */

public class WumpusWorld {

	private Tile[][] map;
	private int playerX;
	private int playerY;
	private char playerOR;
	private boolean isGameOver;
	private boolean hasGold;

	/**
	 * Creates a hardcoded 5x5 wumpus world map
	 * Player starts at 0,0 and has an Orientation 'e'
	isGameOver = false;
	 */
	public WumpusWorld() {
		GenerateValidWorld(50);

		playerX = 0;
		playerY = 0;
		map[playerX][playerY].setVisible();
		playerOR = 'e';

		isGameOver = false;
	}

	/**
	 * Returns a Tile at the specified position
	 * @param x
	 * @param y
	 * @return Tile[x][y]
	 */
	public Tile getTile(int x, int y) {
		return map[x][y];
	}

	/**
	 * Returns the entire map
	 * @return Tile[][]
	 */
	public Tile[][] getmap() {
		return map;
	}
	
	/**
	 * Moves the player based on the direction
	 * if the player orientation is different the direction
	 * then change the orientation
	 * @param direction
	 * @return Boolean if the player moved or not
	 */
	public boolean move(int direction) {
		switch (direction) {
		// move up
		case 0:
			if (playerOR == 's') {
				// actually move
				if(isValidMove(0)){
					playerY++;
					map[playerX][playerY].setVisible();
					return true;
				}
				
				return false;
			} else {
				// set orientation
				playerOR = 's';
				return false;
			}
		// move right
		case 1:
			if (playerOR == 'e') {
				if(isValidMove(1)){
					playerX++;
					map[playerX][playerY].setVisible();
					return true;
				}
				
				return false;
			} else {
				playerOR = 'e';
				return false;
			}
		// move down
		case 2:
			if (playerOR == 'n') {
				if(isValidMove(2)){
					playerY--;
					map[playerX][playerY].setVisible();
					return true;
				}
				
				return false;
			} else {
				playerOR = 'n';
				return false;
			}
		// move left
		case 3:
			if (playerOR == 'w') {
				if(isValidMove(3)){
					playerX--;
					map[playerX][playerY].setVisible();
					return false;
				}
				
				return false;
			} else {
				playerOR = 'w';
				return false;
			}
		default:
			return false;
		}
		
	}
	
	public boolean hasWon(){
		Objectives obj = map[playerX][playerY].getObjective();
		
		if(obj == Objectives.Latter && hasGold){
			return true;
		}
		return false;
	}
	
	public boolean hasGold(){
		Objectives obj = map[playerX][playerY].getObjective();
		
		if(obj == Objectives.Gold){
			hasGold = true;
		}
		
		return hasGold;
	}

	public Point getPlayerPosition() {
		return new Point(playerX, playerY);
	}

	public int getPlayerX() {
		return playerX;
	}

	public int getPlayerY() {
		return playerY;
	}

	public char getPlayerOrientation() {
		return playerOR;
	}
	
	/**
	 * Checks surrounding tiles for objectives
	 * @return ArrayList of the perceived objectives
	 */
	public Collection<Percepts> getPerceptions() {
		HashSet<Percepts> percepts = new HashSet<Percepts>();
		
		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				int px = playerX + x;
				int py = playerY + y;
				
				if (px < 0 || py < 0 || px > 4 || py > 4) {
					continue;
				}
				
				if (Math.abs(x) == Math.abs(y)) {
					continue;
				}
				
				Objectives obj = getTile(px, py).getObjective();
				
				if (obj == Objectives.Gold) {
					percepts.add(Percepts.Glitter);
				}
				
				if (obj == Objectives.Wumpus) {
					percepts.add(Percepts.Stench);
				}
				
				if (obj == Objectives.Pit) {
					percepts.add(Percepts.Breeze);
				}
			}
		}
		
		return percepts;
	}
	
	public boolean isValidMove(int move){
		switch (move){
		case 0:
			if(playerY + 1 <= 4){
				return true;
			}
			return false;
		case 1:
			if(playerX + 1 <= 4){
				return true;
			}
			return false;
		case 2: 
			if(playerY - 1 >= 0){
				return true;
			}
			return false;
		case 3: 
			if(playerX - 1 >= 0){
				return true;
			}
			return false;
		default:
			return false;
		}
	}
	
	public boolean isGameOver(){
		Objectives obj = map[playerX][playerY].getObjective();
		
		switch(obj){
		
		case Wumpus:
			isGameOver = true;
			return isGameOver;
		case Pit:
			isGameOver = true;
			return isGameOver;
		default:
			return isGameOver;
		}
		
	}

	public boolean getGameOver(){
		return isGameOver;
	}

	private void printWorldObjectives(){
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map.length; x++){
				System.out.print("[" + getTile(x,y).getObjective().toString().charAt(0) + "]");
			}
			System.out.println(' ');
		}
	}

	/**
	 * Generates a solvable pseudo random world of width x width dimensions
	 * @param width
     */
	private void GenerateValidWorld(int width){
		boolean currentMapValid = false;
		int failedCount = 0;

		while ( !currentMapValid ) {
			//Continue for lazy collision detection
			if ( !GenerateRandomWorld(width) ) {
				failedCount++;
				continue;
			}

			//Map didn't fail to generate, validate
			printWorldObjectives();
			currentMapValid = CurrentMapIsValid();
			System.out.println("--------");

			failedCount++;
		}

		System.out.println("Failed: " + failedCount + " times");

	}

	/**
	 * BFS to validate a map can be solved
	 * @return
     */
	private boolean CurrentMapIsValid(){
		ArrayList<Tile> queue = new ArrayList<>();
		HashMap<Tile, Boolean> visited = new HashMap<>();

		boolean latterFound = false;
		boolean goldFound = false;

		queue.add(map[0][0]);

		while ( !queue.isEmpty() ) {
			Tile curTile = queue.get(0);

			visited.put(curTile, true);

			int x = curTile.getX();
			int y = curTile.getY();

			if ( curTile.getObjective() == Objectives.Gold ) {
				goldFound = true;
			}

			if ( curTile.getObjective() == Objectives.Latter ) {
				latterFound = true;
			}

			if ( goldFound && latterFound ) {
				return true;
			}

			//Check/add square above
			if ( y - 1 > 0 && isOpenTile(map[y-1][x]) ) {
				if ( !visited.containsKey(map[y-1][x]) ) {
					queue.add(map[y-1][x]);
				}
			}

			//Check/add square below
			if ( y + 1 < map.length && isOpenTile(map[y+1][x]) ) {
				if ( !visited.containsKey(map[y+1][x]) ) {
					queue.add(map[y+1][x]);
				}
			}

			//Check/add square left
			if ( x - 1 > 0 && isOpenTile(map[y][x-1]) ) {
				if ( !visited.containsKey(map[y][x-1]) ) {
					queue.add(map[y][x-1]);
				}
			}

			//Check/add square right
			if ( x + 1 < map.length && isOpenTile(map[y][x+1]) ) {
				if ( !visited.containsKey(map[y][x+1]) ) {
					queue.add(map[y][x+1]);
				}
			}

			queue.remove(0);
		}

		return false;
	}

	private boolean isOpenTile(Tile t){
		return t.getObjective() != Objectives.Wumpus && t.getObjective() != Objectives.Pit;
	}

	/**
	 * Generate a random width x width world with pit probability pitProbabily
	 * @param width
     */
	boolean GenerateRandomWorld(int width){
		double pitProbability = 0.15; //TODO: Could be a constant at top of file

		map = new Tile[width][width];

		//Choose pseudo random locations for objectives
		int wumpusX = (int) (Math.random() * width);
		int wumpusY = (int) (Math.random() * width);

		int goldX = (int) (Math.random() * width);
		int goldY = (int) (Math.random() * width);

		int latterX = (int) (Math.random() * width);
		int latterY = (int) (Math.random() * width);


		//TODO: Prettier collision prevention?
		// Wumpus on gold, lazy restart
		if ( goldX == wumpusX && goldY == wumpusY ) {
			return false;
		}

		// Gold on latter, lazy restart
		if ( goldX == latterX && goldY == latterY ) {
			return false;
		}

		// Latter on wumpus, lazy restart
		if ( latterX == wumpusX && latterY == wumpusY ) {
			return false;
		}

		if ( latterX == 0 && latterY == 0 ){
			return false;
		}

		if ( wumpusX == 0 && wumpusY == 0 ) {
			return false;
		}

		if ( goldX == 0 && goldY == 0 ) {
			return false;
		}

		//Generate map
		for(int y = 0; y < width; y++){
			for(int x = 0; x < width; x++) {
				Tile curTile = new Tile(x, y);

				//Simple psuedorandom pit generator.
				boolean isPit = Math.random() <= pitProbability;

				//Nothing on start
				if (x == 0 && y == 0) {
					isPit = false;
				}

				//Place one objective max per tile
				if (x == wumpusX && y == wumpusY) {
					curTile.setObjective(Objectives.Wumpus);
				} else if (x == goldX && y == goldY) {
					curTile.setObjective(Objectives.Gold);
				} else if (x == latterX && y == latterY ) {
					curTile.setObjective(Objectives.Latter);
				} else if ( isPit ) {
					curTile.setObjective(Objectives.Pit);
				} else {
					curTile.setObjective(Objectives.Empty);
				}

				map[y][x] = curTile;
			}
		}

		return true;
	}
}
