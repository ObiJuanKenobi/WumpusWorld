package graphics;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import math.Vector2f;

/**
 * GLFW callback for handling mouse position
 * @author Team Bits Please
 *
 */
public class MousePos extends GLFWCursorPosCallback {
	
	private static Vector2f mousePos = new Vector2f(-1.0f, 1.0f);

	/**
	 * Get called whenever GLFW detects mouse movement
	 */
	public void invoke(long window, double xpos, double ypos) {
		if (xpos < 0 || xpos >= Window.getWidth() || ypos < 0 || ypos >= Window.getHeight()) {
			mousePos.setX(-1.0f);
			mousePos.setY(-1.0f);
		} else {
			mousePos.setX((float) xpos);
			mousePos.setY((float) ypos);
		}
	}
	
	/**
	 * Gets the most recent position of the mouse
	 * @return Most recent position of mouse
	 */
	public static Vector2f getMousePosition() {
		return mousePos;
	}

}
