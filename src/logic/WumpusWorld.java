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

	/**
	 * Creates a hardcoded 5x5 wumpus world map
	 * Player starts at 0,0 and has an Orientation 'e'
	isGameOver = false;
	 */
	public WumpusWorld() {
		GenerateValidWorld(5);
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

//	//like the map on the website
//	private void Generatemap() {
//		map = new Tile[5][5];
//		for (int i = 0; i < 5; i++) {
//			for (int j = 0; j < 5; j++) {
//				map[i][j] = new Tile(i, j);
//			}
//		}
//		setUpObjectives();
//		setUpPercepts();
//		playerX = 0;
//		playerY = 0;
//		map[playerX][playerY].setVisible();
//		playerOR = 'e';
//	}
//
//	private void setUpPercepts() {
//		map[0][1].addPrecept(Percepts.Stench);
//		map[0][3].addPrecept(Percepts.Stench);
//		map[0][2].addPrecept(Percepts.Glitter);
//		map[0][4].addPrecept(Percepts.Breeze);
//		map[1][0].addPrecept(Percepts.Breeze);
//		map[1][1].addPrecept(Percepts.Glitter);
//		map[1][2].addPrecept(Percepts.Stench);
//		map[1][2].addPrecept(Percepts.Breeze);
//		map[1][3].addPrecept(Percepts.Breeze);
//		map[1][3].addPrecept(Percepts.Glitter);
//		map[2][1].addPrecept(Percepts.Breeze);
//		map[2][3].addPrecept(Percepts.Breeze);
//		map[2][4].addPrecept(Percepts.Breeze);
//		map[3][0].addPrecept(Percepts.Breeze);
//		map[3][2].addPrecept(Percepts.Breeze);
//		map[3][4].addPrecept(Percepts.Breeze);
//		map[4][3].addPrecept(Percepts.Breeze);
//	}
//
//	private void setUpObjectives() {
//		map[0][2].setObjective(Objectives.Wumpus);
//		map[1][2].setObjective(Objectives.Gold);
//		map[1][4].setObjective(Objectives.Pit);
//		map[2][1].setObjective(Objectives.Pit);
//		map[2][2].setObjective(Objectives.Pit);
//		map[3][3].setObjective(Objectives.Pit);
//	}

	private void printWorldPercepts(){
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map.length; x++){
				System.out.print('[');
				for(int i = 0; i < getTile(x,y).getPercepts().length; i++ ) {
					System.out.print('.' + getTile(x,y).getPercepts()[i].toString() + '.');
				}
				System.out.print(']');
			}
			System.out.println(' ');
		}
	}

	private void printWorldObjectives(){
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map.length; x++){
				System.out.print('[' + getTile(x,y).getObjective().toString() + ']');
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

		while ( !currentMapValid ) {
			GenerateRandomWorld(width);
			currentMapValid = CurrentMapIsValid();
		}

		//Now add precepts
		AddPreceptsToMap();
	}

	/**
	 * BFS to validate a map can be solved
	 * @return
     */
	private boolean CurrentMapIsValid(){
		ArrayList<Tile> queue = new ArrayList<>();
		HashMap<Tile, Boolean> visited = new HashMap<>();

		queue.add(map[0][0]);

		while ( !queue.isEmpty() ) {
			Tile curTile = queue.get(0);

			visited.put(curTile, true);

			int x = curTile.getX();
			int y = curTile.getY();

			if ( curTile.getObjective() == Objectives.Gold ) {
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
	 * Given a map, fill in its percepts
	 */
	private void AddPreceptsToMap(){
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map.length; x++){
				Tile cur = map[y][x];
				ArrayList<Percepts> curPercept = new ArrayList<>();

				if ( map[y][x].getPercepts() == null ) {
					map[y][x].setPercepts(curPercept);
				}

				//Determine percept
				switch ( cur.getObjective() ) {
					case Wumpus:
						curPercept.add(Percepts.Stench);
						break;
					case Pit:
						curPercept.add(Percepts.Breeze);
						break;
					default:
						break;

				}

				//Set neighbors w/ valid percept
				if ( x - 1 > 0 && map[y][x-1].getObjective() == Objectives.Empty) {
					for(int i = 0; i < map[y][x-1].getPercepts().length; i++ ) {
						if ( !curPercept.contains(map[y][x-1].getPercepts()[i])) {
							curPercept.add(map[y][x-1].getPercepts()[i]);
						}
					}

					map[y][x-1].setPercepts(curPercept);
				}

				if ( x + 1 < (map.length - 1) && map[y][x+1].getObjective() == Objectives.Empty) {
					for(int i = 0; i < map[y][x+1].getPercepts().length; i++ ) {
						if ( !curPercept.contains(map[y][x+1].getPercepts()[i])) {
							curPercept.add(map[y][x+1].getPercepts()[i]);
						}
					}

					map[y][x+1].setPercepts(curPercept);
				}

				if ( y + 1 < (map.length - 1) && map[y+1][x].getObjective() == Objectives.Empty) {
					for(int i = 0; i < map[y+1][x].getPercepts().length; i++ ) {
						if ( !curPercept.contains(map[y+1][x].getPercepts()[i])) {
							curPercept.add(map[y+1][x].getPercepts()[i]);
						}
					}

					map[y+1][x].setPercepts(curPercept);
				}

				if ( y - 1 > 0 && map[y-1][x].getObjective() == Objectives.Empty) {
					for(int i = 0; i < map[y-1][x].getPercepts().length; i++ ) {
						if ( !curPercept.contains(map[y-1][x].getPercepts()[i])) {
							curPercept.add(map[y-1][x].getPercepts()[i]);
						}
					}

					map[y-1][x].setPercepts(curPercept);
				}

			}
		}
	}

	/**
	 * Generate a random width x width world with pit probability pitProbabily
	 * @param width
     */
	private void GenerateRandomWorld(int width){
		double pitProbability = 0.50; //TODO: Could be a constant at top of file

		map = new Tile[width][width];

		//Choose psuedorandom wumpus location
		int wumpusX = (int) (Math.random() * width);
		int wumpusY = (int) (Math.random() * width);

		//Choose psuedorandom gold location
		int goldX = (int) (Math.random() * width);
		int goldY = (int) (Math.random() * width);

		//Wumpus on gold, lazy restart
		if ( goldX == wumpusX && goldY == wumpusY ) {
			return;
		}

		//Generate map
		for(int y = 0; y < width; y++){
			for(int x = 0; x < width; x++) {
				Tile curTile = new Tile(x,y);

				//Nothing on start
				if ( x == 0 && y == 0 ) {
					curTile.setObjective(Objectives.Empty);
					map[y][x] = curTile;
					continue;
				}

				//Simple psuedorandom pit generator.
				boolean isPit =  Math.random() <= pitProbability;

				//Place one objective max per tile
				if ( x == wumpusX && y == wumpusY ) {
					curTile.setObjective(Objectives.Wumpus);
				} else if ( x == goldX && y == goldY ){
					curTile.setObjective(Objectives.Gold);
				} else if ( isPit ) {
					curTile.setObjective(Objectives.Pit);
				} else {
					curTile.setObjective(Objectives.Empty);
				}

				map[y][x] = curTile;
			}
		}

		playerX = 0;
		playerY = 0;
		map[playerX][playerY].setVisible();
		playerOR = 'e';
	}
}
