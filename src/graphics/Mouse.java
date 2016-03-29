package graphics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 * GLFW callback for handling mouse clicks
 * @author Team Bits Please
 *
 */
public class Mouse extends GLFWMouseButtonCallback {
	
	public static final int LEFT_CLICK = GLFW.GLFW_MOUSE_BUTTON_1;
	public static final int RIGHT_CLICK = GLFW.GLFW_MOUSE_BUTTON_2;
	public static final int MIDDLE_CLICK = GLFW.GLFW_MOUSE_BUTTON_3;
	
	public static final int NUM_BUTTONS = 16;
	public static boolean[] buttons = new boolean[NUM_BUTTONS];
	public static boolean[] buttonsDown = new boolean[NUM_BUTTONS];

	/**
	 * Called whenever GLFW detects a mouse click
	 */
	public void invoke(long window, int button, int action, int mods) {
		if (action == GLFW.GLFW_RELEASE) {
			buttons[button] = false;
		} else {
			buttons[button] = true;
		}
	}
	
	/**
	 * Checks state of given mouse button
	 * @param mouseButton Mouse button to check
	 * @return True if clicked down, false otherwise
	 */
	public static boolean getMouse(int mouseButton) {
		return buttons[mouseButton];
	}

}
