package graphics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouse extends GLFWMouseButtonCallback {
	
	public static final int LEFT_CLICK = GLFW.GLFW_MOUSE_BUTTON_1;
	public static final int RIGHT_CLICK = GLFW.GLFW_MOUSE_BUTTON_2;
	public static final int MIDDLE_CLICK = GLFW.GLFW_MOUSE_BUTTON_3;
	
	public static final int NUM_BUTTONS = 16;
	public static boolean[] buttons = new boolean[NUM_BUTTONS];
	public static boolean[] buttonsDown = new boolean[NUM_BUTTONS];

	public void invoke(long window, int button, int action, int mods) {
		if (action == GLFW.GLFW_RELEASE) {
			buttons[button] = false;
		} else {
			buttons[button] = true;
		}
	}
	
	public static boolean getMouse(int mouseButton) {
		return buttons[mouseButton];
	}

}
