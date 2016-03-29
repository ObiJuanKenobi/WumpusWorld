package logic;

public class Dungeon {

	private int playerX;
	private int playerY;
	private int size;
	private Tile[] map;
	
	public Dungeon(int size) {
		map = new Tile[size * size];
		this.size = size;
		playerX = 0;
		playerY = 0;
	}
	
	public void populate() {
		for (int i = 0; i < map.length; i++) {
			map[i] = new Tile();
		}
	}
	
	public Tile getTile(int x, int y) {
		return map[x + y * size];
	}
	
	public Tile[] getMap() {
		return map;
	}
	
	public void move(int dir) {
		switch(dir) {
		case 0:
			playerY++;
			break;
		case 1:
			playerX++;
			break;
		case 2:
			playerY--;
			break;
		case 3:
			playerX--;
			break;
		}
	}
	
	
}
