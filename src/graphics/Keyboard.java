package graphics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * GLFW callback for handling keyboard input
 * @author Team Bits Please
 *
 */
public class Keyboard extends GLFWKeyCallback {

	private static final int NUM_KEYCODES = 512;
	public static boolean[] keys = new boolean[NUM_KEYCODES];

	/**
	 * Called whenever GLFW detects a key press
	 */
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if(action == GLFW.GLFW_RELEASE) {
			keys[key] = false;
		} else {
			keys[key] = true;
		}
	}
	
	/**
	 * Gets status of given key
	 * @param keyCode Key to be checked
	 * @return True if pressed, false otherwise
	 */
	public static boolean getKey(int keyCode) {
		return keys[keyCode];
	}

}
