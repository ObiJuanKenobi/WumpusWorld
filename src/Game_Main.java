import java.util.ArrayList;

import graphics.GLPanel;
import graphics.Mouse;
import graphics.MousePos;
import graphics.GLView.GLViewOnClickListener;
import graphics.Vector2f;
import graphics.Vector4f;
import graphics.Window;

public class Game_Main {
	
	private boolean running = false;
	
	//We will probably want to change this to a 2D array
	// for more intuitive correspondence to the display
	private static ArrayList<GLPanel> gridPanels = new ArrayList<GLPanel>();
	
	private static int boardSize = 5;
	
	public static void main(String[] args) {
		Game_Main game = new Game_Main();
		game.start();
	}
	
	public Game_Main() {
		Window.createWindow(500, 500, "Wumpus World - Game");
		Window.setClearColor(128, 128, 128);
		System.out.println(Window.getOpenGLVersion());
		
		initPanels(boardSize);
	}

	public void run() {
		while(running) {
			update();
			render();
		}
	}
	
	public void update() {
		if (Window.isCloseRequested()) {
			running = false;
		}
		
		if (Mouse.getMouse(Mouse.LEFT_CLICK)) {
			Vector2f mouse = MousePos.getMousePosition();
			int tileX = (int) mouse.getX() / 100;
			int tileY = (int) mouse.getY() / 100;
			System.out.println("X: " + tileX + " Y: " + tileY);
		}
		
	}
	
	public void render() {
		Window.clear();
		
		//TODO - Draw player
		
		for(GLPanel panel : gridPanels){
			panel.Draw();
		}
				
		Window.render();
	}
	
	public void start() {
		running = true;
		run();
	}
	
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
				
				final int x = xIndex;
				final int y = yIndex;
				
				//We may not even want to use a listener in this way or at all..
				//Just a debugging listener for now
				GLViewOnClickListener listener = new GLViewOnClickListener(){

					@Override
					public void onClick() {
						System.out.println("" + x + ", " + y + " clicked");
					}
					
				};
				
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
