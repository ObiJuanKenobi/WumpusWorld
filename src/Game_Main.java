import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import graphics.GLPanel;
//import graphics.GLView.GLViewOnClickListener;
import graphics.Mouse;
import graphics.MousePos;
import graphics.Player;
import graphics.Shader;
import graphics.Window;
import math.Vector2f;
import math.Vector4f;

/**
 * Main runnable class for Wumpus World
 * @author Team Bits Please
 *
 */
public class Game_Main {
	
	private boolean running = false;
	private Player player;
	
	//We will probably want to change this to a 2D array
	// for more intuitive correspondence to the display
	private static ArrayList<GLPanel> gridPanels = new ArrayList<GLPanel>();
	
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
		Window.setClearColor(128, 128, 128);
		System.out.println(Window.getOpenGLVersion());
		
		Shader.loadAll();
		Shader.PLAYER.setUniform1i("tex", 1);
		
		player = new Player(0, 0);
		initPanels(boardSize);
	}

	/**
	 * Primary game loop that alternates between updating and rendering
	 */
	public void run() {
		while(running) {
			update();
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
			Vector2f mouse = MousePos.getMousePosition();
			int tileX = (int) mouse.getX() / 100;
			int tileY = (int) mouse.getY() / 100;
			player.setPosition(tileX, tileY);
			System.out.println("X: " + tileX + " Y: " + tileY);
		}
		
		
		player.update();
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
			System.out.println(error);
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
	private static void initPanels(int dimension){
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
				
				//final int x = xIndex;
				//final int y = yIndex;
				
				//We may not even want to use a listener in this way or at all..
				//Just a debugging listener for now
//				GLViewOnClickListener listener = new GLViewOnClickListener(){
//
//					@Override
//					public void onClick() {
//						System.out.println("" + x + ", " + y + " clicked");
//					}
//					
//				};
				
				gridPanels.add(panel);
				
			}
		}
	}
	
	//Mouse callbacks:
	/*
	 
	 void cursor_position_callback(GLFWwindow* window, double xpos, double ypos)
	{
		mouseX = xpos / WIDTH;
		mouseX = ((mouseX - .5f) / .5f);
	
		mouseY = ypos / HEIGHT;
		mouseY = ((mouseY - .5f) / -.5f);
	
	
	}
	
	void mouse_button_callback(GLFWwindow* window, int button, int action, int mods)
	{
		if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
			glm::vec3 mouseClick = glm::vec3(mouseX, mouseY, 0.0f);
			for (std::vector<GLView*>::iterator it = views.begin(); it != views.end(); it++) {
				if ((*it)->CheckClicked(mouseClick)) {
					(*it)->OnClick(mouseClick);
				}
			}
	
		}
	}
	 */

}
