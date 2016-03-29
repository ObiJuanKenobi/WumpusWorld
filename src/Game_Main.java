import graphics.Window;

public class Game_Main {
	
	private boolean running = false;
	
	public static void main(String[] args) {
		Game_Main game = new Game_Main();
		game.start();
	}
	
	public Game_Main() {
		Window.createWindow(960, 540, "Wumpus World - Game");
		Window.setClearColor(128, 128, 128);
		System.out.println(Window.getOpenGLVersion());
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
	}
	
	public void render() {
		Window.clear();
		Window.render();
	}
	
	public void start() {
		running = true;
		run();
	}

}
