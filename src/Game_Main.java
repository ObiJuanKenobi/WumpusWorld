import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import AI.AI;
import AI.AI.AiDifficulty;
import AI.GlobalAI;
import AI.GlobalDfsAI;
import AI.GreedyAI;
import graphics.GLIcon;
import graphics.GLPanel;
import graphics.Mouse;
import graphics.MousePos;
import graphics.Player;
import graphics.Shader;
import graphics.StartMenu;
import graphics.Window;
import logic.Objectives;
import logic.Percepts;
import logic.WumpusWorld;
import math.Vector2f;
import math.Vector3f;

/**
 * Main runnable class for Wumpus World
 * @author Team Bits Please
 *
 */
public class Game_Main {
	private final int UPDATES_PER_SEC = 60;
	private final int UPDATE_TIME_NS = 1000000000 / UPDATES_PER_SEC;
	
	private StartMenu menu;
	
	private boolean running = true;
	private WumpusWorld world;
	private Player player;
	
	private ArrayList<GLPanel> gridPanels = new ArrayList<GLPanel>();
	private int windowWidth = 1600, windowHeight = 1600;
	private float panelWidth, panelHeight;
	
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
		Window.createWindow(windowWidth, windowHeight, "Wumpus World - Game");
		Window.setClearColor(50, 50, 50);
		System.out.println(Window.getOpenGLVersion());
		
		Shader.loadAll();
		Shader.PLAYER.setUniform1i("tex", 1);
		
		initPanels(boardSize);
		
		System.out.println("Welcome to the Wumpus World!");
	}

	/**
	 * Primary game loop that alternates between updating and rendering
	 */
	public void run() {
		
		//Display start/difficulty screen:
		while(!menu.isStarted() && running){
			Window.clear();
			menu.Draw();
			if(Mouse.getMouse(Mouse.LEFT_CLICK)) { 
				Vector2f screen = MousePos.getMousePosition();
				Vector2f ndc = new Vector2f(screen.x / windowWidth * 2.0f - 1.0f, screen.y / windowHeight * -2.0f + 1.0f);
				//System.out.println("" + ndc.x  + " , " + ndc.y);
				menu.CheckClicked(ndc);
			}
			Window.render();
			checkClose();
		}
		
		//Set game difficulty:
		world = new WumpusWorld(menu.getDifficulty());
		player = new Player(world.getPlayerPosition().x, world.getPlayerPosition().y);
		
		if(menu.getAiDifficulty() == AiDifficulty.none){
			playSingle();
		}
		else {
			playAi(menu.getAiDifficulty());
		}
		
		//Cleanup resources:
		deallocate();
	}
	
	private void playAi(AiDifficulty diff){
		System.out.println("Playing the AI");
		WumpusWorld aiWorld = WumpusWorld.copy(world);
		AI ai = null;
		switch(diff){
			case easy:
				ai = new GreedyAI(aiWorld);
				break;
				
			case medium:
				ai = new GlobalAI(aiWorld);
				break;
				
			case hard:
				ai = new GlobalDfsAI(aiWorld);
				break;
			
			default:
				//only those options should be possible
				break;
		}
		
		//Alternate turns between AI and user:
		long startTime = System.nanoTime();
		long elapsedTime = 0;
		
		//Prevent player from moving while board is rendering
		Mouse.buttons[0] = false;

		//Begin game:
		while(running && !world.isGameOver() && !aiWorld.isGameOver()) {
			elapsedTime = System.nanoTime() - startTime;
			
			if (elapsedTime >= UPDATE_TIME_NS) {
				startTime = System.nanoTime();
				elapsedTime = 0;
				
				//player's turn:
				while(!update())
					render(); //wait for update to return true, means user clicked
				
				//AI's turn:
				ai.makeMove();
			}
			
			render();
		}
		
		if(aiWorld.isGameOver()){
			if(!aiWorld.haveWon() && !world.isGameOver()){
				//both games over, neither won, tie:
				System.out.println("You beat the AI");
			}
			else if(!aiWorld.haveWon()){
				System.out.println("You tied the AI!");
			}
			else if(aiWorld.haveWon() && !world.haveWon()){
				System.out.println("The AI has won");
			}
			else {
				System.out.println("You tied the AI");
			}
		}
		else if (world.isGameOver()){
			if(world.haveWon()){
				System.out.println("You beat the AI");
			}
			else {
				System.out.println("The AI has won");
			}
		}
		
	}
	
	private void playSingle(){
		
		System.out.println("Playing solo");
		
		long startTime = System.nanoTime();
		long elapsedTime = 0;
		
		//Prevent player from moving while board is rendering
		Mouse.buttons[0] = false;

		//Begin game:
		while(running && !world.isGameOver()) {
			elapsedTime = System.nanoTime() - startTime;
			
			if (elapsedTime >= UPDATE_TIME_NS) {
				startTime = System.nanoTime();
				elapsedTime = 0;
				update();
			}
			
			render();
		}
	}
	
	private void playMulti(){
		System.out.println("Playing multiplayer");
		//TODO
	}
	
	private void deallocate(){

		for(GLPanel panel : gridPanels){
			panel.Deallocate();
		}
		
		menu.Deallocate();
		
		Window.dispose();
	}
	
	private void checkClose(){
		if(Window.isCloseRequested()){
			running = false;
		}
	}
	
	
	/**
	 * Returns true if player moved/changed orientation
	 * @return
	 */
	public boolean update() {
		boolean moved = false;
		if (Window.isCloseRequested()) {
			running = false;
		}
		
		if (Mouse.getMouse(Mouse.LEFT_CLICK)) {
			moved = true;
			
			// calculate tile of click
			Vector2f mouse = MousePos.getMousePosition();
			int tileX = (int) (mouse.x / panelWidth);
			int tileY = (int) (mouse.y / panelHeight);
			
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
			System.out.println("Direction: " + world.getplayerOrientation());			
			System.out.println();
			
			
			// wait shortly to prevent click spamming
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		player.update(world.getplayerOrientation());
		world.hasGold();
		
		//Update tiles: 
		
		int panelIndex = world.getPlayerX() + world.getPlayerY() * 5;
		GLPanel currentPanel = 	gridPanels.get(panelIndex);
		
		// check surroundings
		Collection<Percepts> percepts = world.getPerceptions(world.getPlayerX(), world.getPlayerY());
		
		// check current tile
		Objectives objective = world.getTile(world.getPlayerX(), world.getPlayerY()).getObjective();
		
		
		if(!currentPanel.isDiscovered()){
			currentPanel.discover();
			//System.out.println(currentPanel.xIndex + ", " + currentPanel.yIndex);
			
			if(objective != null && objective != Objectives.Empty){
				
				if (objective == Objectives.Gold) {
					System.out.println("You have found the gold!");
					//running = false;
				}
				if (objective == Objectives.Pit) {
					System.out.println("You fell into a pit and died!");
					//running = false;
				}
				if (objective == Objectives.Wumpus) {
					System.out.println("You have been eaten by the Wumpus!");
					//running = false;
				}
				
				currentPanel.AddView(new GLIcon(.8f * gridPanels.get(panelIndex).GetWidth(), .8f * gridPanels.get(panelIndex).GetHeight(), objective));
			}
			
			else {
				for(Percepts percept : percepts){
					
					if(percept == Percepts.Glitter) {
						System.out.println("You see a faint glittering.");
					}
					if(percept == Percepts.Breeze) {
						System.out.println("You hear a strong breeze.");
					}
					if(percept == Percepts.Stench) {
						System.out.println("You smell a powerful stench.");
					}
					
					currentPanel.AddView(new GLIcon(.2f * gridPanels.get(panelIndex).GetWidth(), .2f * gridPanels.get(panelIndex).GetHeight(), percept));
				}
			}
		}
		
		return moved;
		
	}
	
	/**
	 * Renders background, player, and other sprites
	 */
	public void render() {
		Window.clear();
		
		for(GLPanel panel : gridPanels){
			panel.Draw();
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
		menu = new StartMenu();
		//running = true;
		run();
	}
	
	/**
	 * Creates dungeon tiles
	 * @param dimension Size of dungeon, as in N x N
	 */
	private void initPanels(int dimension){
		//In NDC, width of board is 2 (goes from -1 to 1)
		//  so divide into 5 equal sections (we can add padding later)
		panelWidth = 2.0f / (float)dimension;
		
		//Same for height:
		panelHeight = 2.0f / (float)dimension;
		
		float yStart = 1.0f - panelHeight;
		float xStart = -1.0f;
		
		for(int yIndex = 0; yIndex < dimension; yIndex++){
			
			float yPt = yStart - yIndex*panelHeight;
			
			for(int xIndex = 0; xIndex < dimension; xIndex++){
				float xPt = xStart + xIndex*panelWidth;
				
				GLPanel panel = new GLPanel(panelWidth, panelHeight,
						GLPanel.Orientation.vertical, .025f, .025f, .025f, xIndex, yIndex);
				
				//Position the panel:
				panel.Translate(new Vector3f(xPt, yPt, .0f));
	
				panel.InitBuffers();

				gridPanels.add(panel);
				
			}
		}
		
		//Create an icon now so all textures can be generated before gameplay begins
		//GLIcon icon = new GLIcon(.1f, .1f, Percepts.Breeze);
		
		panelWidth = windowWidth / dimension;
		panelHeight = windowHeight / dimension;
	}
	
}
