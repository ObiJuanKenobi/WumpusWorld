import org.lwjgl.glfw.GLFW;

import graphics.Keyboard;
import graphics.Mouse;
import graphics.MousePos;
import graphics.Window;
import logic.Dungeon;

public class Test_Main {

	public static void main(String[] args) {
		Window.createWindow(960, 540, "Wumpus World - Test");
		Window.setClearColor(50, 128, 128);
		Dungeon don = new Dungeon(5);
		
		System.out.println(Window.getOpenGLVersion());
		while (!Window.isCloseRequested()) {
			Window.clear();
			Window.render();
			
			if (Keyboard.getKey(GLFW.GLFW_KEY_SPACE)) {
				don.move(0);
			}
			if (Mouse.getMouse(Mouse.LEFT_CLICK)) {
				//System.out.println("Left click!");
			}
			
			System.out.println(MousePos.getMousePosition());
		}
		
		Window.dispose();
	}

}
