import graphics.Window;

public class Test_Main {

	public static void main(String[] args) {
		Window.createWindow(960, 540, "Wumpus World");
		Window.setClearColor(50, 128, 128);
		
		while (!Window.isCloseRequested()) {
			Window.clear();
			Window.render();
		}
		
		Window.dispose();
	}

}
