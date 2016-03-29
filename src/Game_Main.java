import graphics.Window;

public class Game_Main implements Runnable {
	
	private boolean running = false;
	private Thread thread;
	
	public static void main(String[] args) {
		Game_Main game = new Game_Main();
		game.start();
	}
	
	public Game_Main() {
		// Constuctor
	}
	
	public void init() {
		Window.createWindow(960, 540, "Wumpus World - Game");
		Window.setClearColor(128, 128, 128);
		System.out.println(Window.getOpenGLVersion());
	}

	@Override
	public void run() {
		init();
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
		thread = new Thread(this);
		thread.start();
	}

}
