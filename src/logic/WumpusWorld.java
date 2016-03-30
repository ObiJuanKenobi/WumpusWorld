package logic;

import java.awt.Point;
import java.util.ArrayList;

public class WumpusWorld {

	private Tile[][] world;
	private int playerX;
	private int playerY;
	private char playerOR;

	// A hard coded map 4x4 map
	public WumpusWorld() {
		GenerateWorld();
	}

	public WumpusWorld(int size) {
		// Random world generation
	}

	public Tile getTile(int x, int y) {
		return world[x][y];
	}

	public Tile[][] getWorld() {
		return world;
	}

	public boolean move(int direction) {
		switch (direction) {
		// move up
		case 0:
			if (playerOR == 'n') {
				// actually move
				playerY++;
				return true;
			} else {
				// set orientation
				playerOR = 'n';
				return false;
			}
			// move right
		case 1:
			if (playerOR == 'e') {
				playerX++;
				return true;
			} else {
				playerOR = 'e';
				return false;
			}
			// move down
		case 2:
			if (playerOR == 's') {
				playerY--;
				return true;
			} else {
				playerOR = 's';
				return false;
			}
			// move left
		case 3:
			if (playerOR == 'w') {
				playerX--;
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

	//like the map on the website
	private void GenerateWorld() {
		world = new Tile[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				world[i][j] = new Tile(i, j);
			}
		}
		setUpObjectives();
		setUpPercepts();
		playerX = 0;
		playerY = 0;
		playerOR = 'e';
	}

	private void setUpPercepts() {
		world[0][1].addPrecept(Percepts.Stench);
		world[1][0].addPrecept(Percepts.Breeze);
		world[1][2].addPrecept(Percepts.Stench);
		world[1][2].addPrecept(Percepts.Breeze);
		world[2][1].addPrecept(Percepts.Breeze);
		world[2][3].addPrecept(Percepts.Breeze);
		world[3][0].addPrecept(Percepts.Breeze);
		world[3][2].addPrecept(Percepts.Breeze);
	}

	private void setUpObjectives() {
		world[0][2].setObjective(Objectives.Pit);
		world[0][2].setObjective(Objectives.Wumpus);
		world[1][2].setObjective(Objectives.Gold);
		world[2][2].setObjective(Objectives.Pit);
		world[3][3].setObjective(Objectives.Pit);
		world[1][3].setObjective(Objectives.Ladder);
	}
}
