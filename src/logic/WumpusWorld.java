package logic;

import java.awt.Point;
import java.util.ArrayList;

public class WumpusWorld {

	private Tile[][] map;
	private int playerX;
	private int playerY;
	private char playerOR;

	// A hard coded map 4x4 map
	public WumpusWorld() {
		Generatemap();
	}

	public WumpusWorld(int size) {
		// Random map generation
	}

	public Tile getTile(int x, int y) {
		return map[x][y];
	}

	public Tile[][] getmap() {
		return map;
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
	private void Generatemap() {
		map = new Tile[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				map[i][j] = new Tile(i, j);
			}
		}
		setUpObjectives();
		setUpPercepts();
		playerX = 0;
		playerY = 0;
		playerOR = 'e';
	}

	private void setUpPercepts() {
		map[0][1].addPrecept(Percepts.Stench);
		map[1][0].addPrecept(Percepts.Breeze);
		map[1][2].addPrecept(Percepts.Stench);
		map[1][2].addPrecept(Percepts.Breeze);
		map[2][1].addPrecept(Percepts.Breeze);
		map[2][3].addPrecept(Percepts.Breeze);
		map[3][0].addPrecept(Percepts.Breeze);
		map[3][2].addPrecept(Percepts.Breeze);
	}

	private void setUpObjectives() {
		map[0][2].setObjective(Objectives.Pit);
		map[0][2].setObjective(Objectives.Wumpus);
		map[1][2].setObjective(Objectives.Gold);
		map[2][2].setObjective(Objectives.Pit);
		map[3][3].setObjective(Objectives.Pit);
		map[1][3].setObjective(Objectives.Ladder);
	}
}
