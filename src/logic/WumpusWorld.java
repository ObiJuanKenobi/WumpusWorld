package logic;

import java.awt.Point;
import java.util.ArrayList;
/**
 * @author Paul
 * @author Juan
 * TODO: Handle player movement validation (player doesn't run off map)
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
		Generatemap();
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
				playerY++;
				map[playerX][playerY].setVisible();
				return true;
			} else {
				// set orientation
				playerOR = 's';
				return false;
			}
		// move right
		case 1:
			if (playerOR == 'e') {
				playerX++;
				map[playerX][playerY].setVisible();
				return true;
			} else {
				playerOR = 'e';
				return false;
			}
		// move down
		case 2:
			if (playerOR == 'n') {
				playerY--;
				map[playerX][playerY].setVisible();
				return true;
			} else {
				playerOR = 'n';
				return false;
			}
		// move left
		case 3:
			if (playerOR == 'w') {
				playerX--;
				map[playerX][playerY].setVisible();
				return true;
			} else {
				playerOR = 'w';
				return false;
			}
		}
		// should never get to this point
		return false;
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
	public ArrayList<Percepts> getPerceptions() {
		ArrayList<Percepts> percepts = new ArrayList<Percepts>();
		
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
	
	public boolean isGameOver() {
		return isGameOver;
	}

	//like the map on the website
	private void Generatemap() {
		map = new Tile[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				map[i][j] = new Tile(i, j);
			}
		}
		setUpObjectives();
		setUpPercepts();
		playerX = 0;
		playerY = 0;
		map[playerX][playerY].setVisible();
		playerOR = 'e';
	}

	private void setUpPercepts() {
		map[0][1].addPrecept(Percepts.Stench);
		map[0][3].addPrecept(Percepts.Stench);
		map[0][2].addPrecept(Percepts.Glitter);
		map[0][4].addPrecept(Percepts.Breeze);
		map[1][0].addPrecept(Percepts.Breeze);
		map[1][1].addPrecept(Percepts.Glitter);
		map[1][2].addPrecept(Percepts.Stench);
		map[1][2].addPrecept(Percepts.Breeze);
		map[1][3].addPrecept(Percepts.Breeze);
		map[1][3].addPrecept(Percepts.Glitter);
		map[2][1].addPrecept(Percepts.Breeze);
		map[2][3].addPrecept(Percepts.Breeze);
		map[2][4].addPrecept(Percepts.Breeze);
		map[3][0].addPrecept(Percepts.Breeze);
		map[3][2].addPrecept(Percepts.Breeze);
		map[3][4].addPrecept(Percepts.Breeze);
		map[4][3].addPrecept(Percepts.Breeze);
	}

	private void setUpObjectives() {
		map[0][2].setObjective(Objectives.Wumpus);
		map[1][2].setObjective(Objectives.Gold);
		map[1][4].setObjective(Objectives.Pit);
		map[2][1].setObjective(Objectives.Pit);
		map[2][2].setObjective(Objectives.Pit);
		map[3][3].setObjective(Objectives.Pit);
	}
}
