import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import graphics.GLPanel;
import graphics.Mouse;
import graphics.MousePos;
import graphics.Player;
import graphics.Shader;
import graphics.Window;
import logic.Objectives;
import logic.Percepts;
import logic.Tile;
import logic.WumpusWorld;
import math.Vector2f;
import math.Vector4f;

/**
 * Main runnable class for Wumpus World
 * @author Team Bits Please
 *
 */
public class Game_Main {
	private final int UPDATES_PER_SEC = 60;
	private final int UPDATE_TIME_NS = 1000000000 / UPDATES_PER_SEC;
	
	private boolean running = false;
	private WumpusWorld world;
	private Player player;
	
	private ArrayList<GLPanel> gridPanels = new ArrayList<GLPanel>();
	
	private static int boardSize = 5;
	
	/**
	 * Entry point of program
	 * @param args Standard command-line args
	 */
	public static void main(String[] args) {
		new Game_Main().start();
	}
	
	/**
	 * Basic contructor that initializes resources
	 */
	public Game_Main() {
		Window.createWindow(500, 500, "Wumpus World - Game");
		Window.setClearColor(50, 50, 50);
		System.out.println(Window.getOpenGLVersion());
		
		Shader.loadAll();
		Shader.PLAYER.setUniform1i("tex", 1);
		
		world = new WumpusWorld();
		player = new Player(world.getPlayerPosition().x, world.getPlayerPosition().y);
		initPanels(boardSize);
		
		System.out.println("Welcome to the Wumpus World!");
	}

	/**
	 * Primary game loop that alternates between updating and rendering
	 */
	public void run() {
		long startTime = System.nanoTime();
		long elapsedTime = 0;

		while(running && !world.isGameOver()) {
			elapsedTime = System.nanoTime() - startTime;
			
			if (elapsedTime >= UPDATE_TIME_NS) {
				startTime = System.nanoTime();
				elapsedTime = 0;
				update();
			}
			
			render();
		}
		
		Window.dispose();
	}
	
	/**
	 * Handles user input and updates game logic
	 */
	public void update() {
		if (Window.isCloseRequested()) {
			running = false;
		}
		
		if (Mouse.getMouse(Mouse.LEFT_CLICK)) {
			
			// calculate tile of click
			Vector2f mouse = MousePos.getMousePosition();
			int tileX = (int) mouse.x / 100;
			int tileY = (int) mouse.y / 100;
			
			// handle player movement
			if (tileX > world.getPlayerX()) {
				world.move(1);
			}
			
			if (tileX < world.getPlayerX()) {
				world.move(3);
			}
			
			if (tileY < world.getPlayerY()) {
				world.move(2);
			}
			
			if (tileY > world.getPlayerY()) {
				world.move(0);
			}
			
			// print player's status
			player.setPosition(world.getPlayerX(), world.getPlayerY());
			System.out.println("Location: " + world.getPlayerX() + ", " + world.getPlayerY());
			System.out.println("Direction: " + world.getPlayerOrientation());
			
			// check surroundings
			ArrayList<Percepts> percepts = world.getPerceptions();
			if(percepts.contains(Percepts.Glitter)) {
				System.out.println("You see a faint glittering.");
			}
			if(percepts.contains(Percepts.Glitter)) {
				System.out.println("You hear a strong breeze.");
			}
			if(percepts.contains(Percepts.Stench)) {
				System.out.println("You smell a powerful stench.");
			}
			
			// check current tile
			Tile current = world.getTile(world.getPlayerX(), world.getPlayerY());
			if (current.getObjective() == Objectives.Gold) {
				System.out.println("You have found the gold!");
				running = false;
			}
			if (current.getObjective() == Objectives.Pit) {
				System.out.println("You fell into a pit and died!");
				running = false;
			}
			if (current.getObjective() == Objectives.Wumpus) {
				System.out.println("You have been eaten by the Wumpus!");
				running = false;
			}
			System.out.println();
			
			// wait shortly to prevent click spamming
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		player.update();
	}
	
	/**
	 * Renders background, player, and other sprites
	 */
	public void render() {
		Window.clear();
		
		for(int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				if (world.getTile(x, y).isVisible()) {
					int offset = x + y * boardSize;
					
					// this calculation fixes the indexing bug
					offset += (20 - (y * boardSize * 2));
					
					gridPanels.get((offset)).Draw();
				}
			}
		}
		
		player.render();
		
		int error = GL11.glGetError();
		if (error != GL11.GL_NO_ERROR) {
			//System.out.println(error);
		}
				
		Window.render();
	}
	
	/**
	 * Intuitive method to set running flag and enter run loop
	 */
	public void start() {
		running = true;
		run();
	}
	
	/**
	 * Creates dungeon tiles
	 * @param dimension Size of dungeon, as in N x N
	 */
	private void initPanels(int dimension){
		//In NDC, width of board is 2 (goes from -1 to 1)
		//  so divide into 5 equal sections (we can add padding later)
		float panelWidth = 2.0f / (float)dimension;
		
		//Same for height:
		float panelHeight = 2.0f / (float)dimension;
		
		float yStart = -1.0f;
		float xStart = -1.0f;
		
		for(int yIndex = 0; yIndex < dimension; yIndex++){
			float yPt = yStart + yIndex*panelHeight;
			
			for(int xIndex = 0; xIndex < dimension; xIndex++){
				float xPt = xStart + xIndex*panelWidth;
				
				GLPanel panel = new GLPanel(panelWidth, panelHeight,
						GLPanel.Orientation.vertical, .1f, .1f, .1f, xIndex, yIndex);
				
				//Position the panel:
				panel.Translate(new Vector2f(xPt, yPt));
				
				//Rendering in different colors right now just to show the different panels:
				panel.SetColor(new Vector4f(yIndex*.1f + xIndex*.1f, 
						yIndex*.1f + xIndex*.1f, .0f, 1.0f));
				
				gridPanels.add(panel);
				
			}
		}
	}
}
