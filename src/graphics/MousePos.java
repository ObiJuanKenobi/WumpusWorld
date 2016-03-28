package graphics;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MousePos extends GLFWCursorPosCallback {
	
	private static Vector2f mousePos = new Vector2f(-1.0f, 1.0f);

	public void invoke(long window, double xpos, double ypos) {
		if (xpos < 0 || xpos >= Window.getWidth() || ypos < 0 || ypos >= Window.getHeight()) {
			mousePos.setX(-1.0f);
			mousePos.setY(-1.0f);
		} else {
			mousePos.setX((float) xpos);
			mousePos.setY((float) ypos);
		}
	}
	
	public static Vector2f getMousePosition() {
		return mousePos;
	}

}
