package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
/**
 * @author Team Bitz please
 *
 */

public class WumpusWorld {

	private Tile[][] map;
	private int playerX;
	private int playerY;
	private char playerOrientation;
	private boolean isGameOver;
	private boolean hasGold;
	
	private int mapSize = 5;
	
	public enum Difficulty {easy, medium, hard}
	
	private Difficulty difficulty;

	private boolean debug = true;

	/**
	 * Creates a pseudorandomly generated widthxwidth world
     * 
	 * Player starts at 0,0 and has an Orientation 'e'
	isGameOver = false;
	 */
	public WumpusWorld(Difficulty difficulty) {
		this.difficulty = difficulty;
		
		GenerateValidWorld(mapSize);

		playerX = 0;
		playerY = 0;
		map[playerX][playerY].setVisible();
		playerOrientation = 'e';

		isGameOver = false;
	}
	
	//Creates another world object with the same map
	public static WumpusWorld copy(WumpusWorld world){
		WumpusWorld copy = new WumpusWorld(world.difficulty);
		
		for(int i=0; i<copy.mapSize; i++){
			for(int j=0; j<copy.mapSize; j++){
				copy.map[i][j] = world.getTile(i, j);
			}
		}
		
		return copy;
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
	 * returns number of tiles in a row/column
	 * @return mapSize
	 */
	public int getMapSize(){
		return mapSize;
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
		// move down
		case 0:
			if (playerOrientation == 's') {
				// actually move
				if(isValidMove(0)){
					playerY++;
					map[playerX][playerY].setVisible();
                    hasGold();
					return true;
				}

				debug("Invalid up");
				return false;
			} else {
				// set orientation
				playerOrientation = 's';
				return false;
			}
		// move right
		case 1:
			if (playerOrientation == 'e') {
				if(isValidMove(1)){
					playerX++;
					map[playerX][playerY].setVisible();
                    hasGold();
					return true;
				}

                debug("Invalid right");
				return false;
			} else {
				playerOrientation = 'e';
				return false;
			}
		// move up
		case 2:
			if (playerOrientation == 'n') {
				if(isValidMove(2)){
					playerY--;
					map[playerX][playerY].setVisible();
                    hasGold();
					return true;
				}

                debug("Invalid down");
				return false;
			} else {
				playerOrientation = 'n';
				return false;
			}
		// move left
		case 3:
			if (playerOrientation == 'w') {
				if(isValidMove(3)){
					playerX--;
					map[playerX][playerY].setVisible();
                    hasGold();
					return true;
				}

                isGameOver = true;
                debug("Invalid left " + String.valueOf(playerX) + " " + String.valueOf(playerY));
				return false;
			} else {
				playerOrientation = 'w';
				return false;
			}
		default:
			return false;
		}
		
	}

    /**
     * Whether the tile at the current players position has the gold
     * @return
     */
	public boolean hasGold(){
		Objectives obj = map[playerX][playerY].getObjective();
		
		if(obj == Objectives.Gold){
			hasGold = true;
		}
		
		return hasGold;
	}

    /**
     * X,Y position of player
     * @return
     */
	public Point getPlayerPosition() {
		return new Point(playerX, playerY);
	}

    /**
     * X coordinate of player
     * @return
     */
	public int getPlayerX() {
		return playerX;
	}

    /**
     * Y coordinate of player
     * @return
     */
	public int getPlayerY() {
		return playerY;
	}

    /**
     * Current orientation of the player
     * @return
     */
	public char getplayerOrientation() {
		return playerOrientation;
	}
	
	/**
	 * Checks surrounding tiles for objectives
	 * @return ArrayList of the perceived objectives
	 */
	public Collection<Percepts> getPerceptions(int curX, int curY) {


		HashSet<Percepts> percepts = new HashSet<Percepts>();
		
		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				int px = curX + x;
				int py = curY + y;
				
				if (px < 0 || py < 0 || px > map.length - 1|| py > map.length - 1) {
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

    /**
     * Returns whether or not the desired move is valid.
     * 0 -> Down
     * 1 -> Right
     * 2 -> Up
     * 3 -> Left
     *
     * @param move
     * @return
     */
	public boolean isValidMove(int move){
		switch (move){
		case 0:
			return playerY + 1 < map.length;
		case 1:
            return playerX + 1 < map.length;
		case 2: 
			return playerY - 1 >= 0;
		case 3: 
			return playerX - 1 >= 0;
		default:
			return false;
		}
	}

    /**
     * Return whether or not the game has ended
     * @return
     */
	public boolean isGameOver(){
		Objectives obj = map[playerX][playerY].getObjective();

        if (obj == Objectives.Wumpus || obj == Objectives.Pit){
            isGameOver = true;
        	//debug("You lost! :(");
        }	

		if ( obj == Objectives.Ladder && this.hasGold) {
			isGameOver = true;
        	//debug("You won! :)");
		}

        return isGameOver;
	}

    /**
     * Whether or not we have won the game
     * @return
     */
    public boolean haveWon(){
		if ( map[playerX][playerY].getObjective() == Objectives.Ladder && hasGold ) {
			//debug("You won! :)");
			return true;
		}

        return false;
    }

    /**
     * Used for testing purposes to print the objective at each tile in the map
     */
	public void printWorldObjectives(){
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map.length; x++){
				if ( x == playerX && y == playerY ) {
					System.out.print("[X]");
				} else{
					System.out.print("[" + getTile(x,y).getObjective().toString().charAt(0) + "]");
				}
			}
			debug(" ");
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
			debug("--------");

			failedCount++;
		}

		debug("Failed: " + (failedCount - 1) + " times");

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
		visited.put(map[0][0], true);

		while ( !queue.isEmpty() ) {
			Tile curTile = queue.get(0);

			//debug(queue.size());

			visited.put(curTile, true);

			int x = curTile.getX();
			int y = curTile.getY();

			if ( curTile.getObjective() == Objectives.Gold ) {
				goldFound = true;
			}

			if ( curTile.getObjective() == Objectives.Ladder ) {
				latterFound = true;
			}

			if ( goldFound && latterFound ) {
				return true;
			}

			//Check/add square above
			if ( y - 1 > 0 && isOpenTile(map[y-1][x]) ) {
				if ( !visited.containsKey(map[y-1][x]) ) {
					queue.add(map[y-1][x]);
					visited.put(map[y-1][x], true);
				}
			}

			//Check/add square below
			if ( y + 1 < map.length && isOpenTile(map[y+1][x]) ) {
				if ( !visited.containsKey(map[y+1][x]) ) {
					queue.add(map[y+1][x]);
					visited.put(map[y+1][x], true);
				}
			}

			//Check/add square left
			if ( x - 1 > 0 && isOpenTile(map[y][x-1]) ) {
				if ( !visited.containsKey(map[y][x-1]) ) {
					queue.add(map[y][x-1]);
					visited.put(map[y][x-1], true);
				}
			}

			//Check/add square right
			if ( x + 1 < map.length && isOpenTile(map[y][x+1]) ) {
				if ( !visited.containsKey(map[y][x+1]) ) {
					queue.add(map[y][x+1]);
					visited.put(map[y][x+1], true);
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
	 *
	 * Could be c
	 * @param width
     */
	private boolean GenerateRandomWorld(int width){
		
		double pitProbability = 0.55;

		if(difficulty == Difficulty.easy) {
			pitProbability = 0.15;
		}
		else if(difficulty == Difficulty.medium){
			pitProbability = .35;
		}


		map = new Tile[width][width];

		//Choose pseudo random locations for objectives
		int wumpusX = (int) (Math.random() * width);
		int wumpusY = (int) (Math.random() * width);

		int goldX = (int) (Math.random() * width);
		int goldY = (int) (Math.random() * width);

		int latterX = (int) (Math.random() * width);
		int latterY = (int) (Math.random() * width);


		//TODO: Prettier 'collision' prevention?
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
					curTile.setObjective(Objectives.Ladder);
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

	private void debug(String s){
		if ( debug ) {
			System.out.println(s);
		}
	}
}
